package com.pam.gps

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.pam.gps.model.Coordinate
import com.pam.gps.model.Trip
import com.pam.gps.model.TripDetails
import com.pam.gps.repositories.LocalPhotosRepository
import com.pam.gps.repositories.PhotosRepository
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import timber.log.Timber

@InternalCoroutinesApi
class TrackerService : Service() {

  companion object {
    const val STOP_SERVICE_CODE = "STOP_SERVICE"
    const val START_SERVICE_CODE = "START_SERVICE"
    private var isCreated = false;
    val isRunning get() = TrackerService.isCreated;
  }

  private lateinit var mFusedLocationClient: FusedLocationProviderClient

  private val mLocationRequest = LocationRequest.create().apply {
    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    maxWaitTime = 60 * 60 * 1000
    fastestInterval = 30 * 1000
    interval = 5 * 60 * 1000
  }
  private lateinit var mLocationCallback: LocationCallback

  private lateinit var mCurrentTrip: Trip
  private lateinit var mCurrentTripDetails: TripDetails

  private val mTripsRepository = TripsRepository()
  private val mPhotosRepository = PhotosRepository()
  private val mLocalPhotosRepository = LocalPhotosRepository(this)

  private val mServiceJob = Job()
  private val mServiceScope = CoroutineScope(Dispatchers.Main + mServiceJob)

  private val mCoordinateQueue = mutableListOf<Coordinate>()
  private val mPhotoUriQueue = mutableListOf<Uri>()

  private val currentEpochTime get() = System.currentTimeMillis()

  private var mLastPhotoUploadTime = currentEpochTime

  //We never want to bind it to any lifecycle
  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    TrackerService.isCreated = true;
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    mLocationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        val coordinates = locationResult?.locations?.map { loc -> Coordinate(loc) }
        Timber.d("Received ${coordinates?.size ?: 0} new coordinates")

        if (coordinates != null) {
          mCoordinateQueue.addAll(coordinates)
        }
        this@TrackerService.tryPostCoordinates()
        this@TrackerService.fetchNewPhotos()
        this@TrackerService.tryPostPhotos()
      }
    }
    mServiceScope.launch { loadTripAndDetails() }
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    when (intent.action) {
      START_SERVICE_CODE -> {
        val notification = createNotification()
        startForeground(1, notification)
        startLocationUpdates()
      }
      STOP_SERVICE_CODE -> {
        mFusedLocationClient.flushLocations()
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        stopForeground(true)
        mServiceScope.launch {
          mTripsRepository.finishTrip(mCurrentTrip)
          stopSelf()
        }
      }
    }

    return START_STICKY //calls onCreate and onStartCommand again when app is resurrected
  }

  override fun onDestroy() {
    super.onDestroy()
    TrackerService.isCreated = false;
  }

  private fun createNotification() : Notification {
    val activityIntent = Intent(this, MainActivity::class.java).apply {
      putExtra("SENDER", "Notification")
    }
    val pendingIntent = PendingIntent.getActivity(
      this,
      0,
      activityIntent,
      PendingIntent.FLAG_UPDATE_CURRENT)

    return NotificationCompat
      .Builder(this, getString(R.string.notification_channel_id))
      .setContentTitle(getString(R.string.notification_title))
      .setContentText(getString(R.string.notification_text))
      .setContentIntent(pendingIntent)
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setSmallIcon(R.drawable.ic_notification_icon)
//      .addAction()
      .build()
  }

  private fun startLocationUpdates() {
    mFusedLocationClient.requestLocationUpdates(
      mLocationRequest,
      mLocationCallback,
      mainLooper
    )
  }

  private fun tryPostCoordinates() {
    if (mCoordinateQueue.isEmpty()) {
      Timber.d("No coordinates to post.")
      return
    }
    if (::mCurrentTripDetails.isInitialized)
      mServiceScope.launch {
        withContext(Dispatchers.Default) {
          Timber.d("Sending ${mCoordinateQueue.size} coordinates to the DB.")
          mTripsRepository.addCoordinates(mCurrentTripDetails, mCoordinateQueue.toTypedArray())
        }
        Timber.d("Cleaning coordinate queue.")
        mCoordinateQueue.clear()
      }
  }

  private fun fetchNewPhotos() {
    val newPhotos = mLocalPhotosRepository.getCameraPhotosSince(mLastPhotoUploadTime)
    mPhotoUriQueue.addAll(newPhotos)
  }

  private fun tryPostPhotos() {
    if (mPhotoUriQueue.isEmpty()) {
      Timber.d("No photos to post.")
      return
    }
    if (::mCurrentTrip.isInitialized) {
      Timber.d("Posting ${mPhotoUriQueue.size} photos to trip ${mCurrentTrip.id}.")
      mPhotoUriQueue.forEach { uri ->
        mPhotosRepository.addPhotoToTrip(mCurrentTrip, mCurrentTripDetails, uri)
      }
      Timber.d("Cleaning photo queue")
      mPhotoUriQueue.clear()
      mLastPhotoUploadTime = currentEpochTime
    }
  }

  private suspend fun loadTripAndDetails() {
    mTripsRepository.getCurrentTrip().take(1).collect { list ->
      when {
        list.isEmpty() -> {
          Timber.d("No active trip received from FireBase, creating new one")
          val (x, y) = mTripsRepository.createTrip()
          mCurrentTrip = x
          mCurrentTripDetails = y
          Timber.d("Trip Id:         ${mCurrentTrip.id}")
          Timber.d("Trip details Id: ${mCurrentTripDetails.id}")
        }
        list.size == 1 -> {
          Timber.d("Found an active trip in db!")
          mCurrentTrip = list.first()
          Timber.d("Trip Id:         ${mCurrentTrip.id}")
          mTripsRepository.getTripDetailsForTrip(mCurrentTrip).take(1).collect { details ->
            if (details == null)
              throw Exception("No trip details found for an existing trip!")
            mCurrentTripDetails = details
            Timber.d("Trip details Id: ${mCurrentTripDetails.id}")
          }
        }
        else -> throw Exception("Too many active trips")
      }
    }
  }
}
