package com.pam.gps

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.pam.gps.extensions.toLatLng
import com.pam.gps.model.Coordinate
import com.pam.gps.model.CurrentTrip
import com.pam.gps.model.Picture
import com.pam.gps.repositories.LocalPhotosRepository
import com.pam.gps.repositories.PhotosRepository
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TrackerService : Service() {

  companion object {
    const val STOP_SERVICE_CODE = "STOP_SERVICE"
    const val START_SERVICE_CODE = "START_SERVICE"
    private var isCreated = false
    val isRunning get() = isCreated

    fun start(context: Context) {
      val startIntent = Intent(context, TrackerService::class.java)
        .apply {
          action = START_SERVICE_CODE
        }
      ContextCompat.startForegroundService(context, startIntent)
    }

    fun stop(context: Context) {
      val stopIntent = Intent(context, TrackerService::class.java)
        .apply {
          action = STOP_SERVICE_CODE
        }
      ContextCompat.startForegroundService(context, stopIntent)
    }
  }

  private lateinit var mFusedLocationClient: FusedLocationProviderClient

  private val mLocationRequest = LocationRequest.create().apply {
    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    maxWaitTime = 60 * 60 * 1000
    fastestInterval = 30 * 1000
    interval = 5 * 60 * 1000
  }
  private lateinit var mLocationCallback: LocationCallback

  private lateinit var mCurrentTrip: CurrentTrip

  private val mTripsRepository = TripsRepository()
  private val mPhotosRepository = PhotosRepository()
  private val mLocalPhotosRepository = LocalPhotosRepository(this)

  private val mServiceJob = Job()
  private val mServiceScope = CoroutineScope(Dispatchers.Main + mServiceJob)

  private val mCoordinateQueue = mutableListOf<Coordinate>()
  private val mPhotoQueue = mutableListOf<Picture>()

  private val currentEpochTime get() = System.currentTimeMillis()

  private var mLastPhotoUploadTime = currentEpochTime
  private var mLastKnownLocation = Coordinate(GeoPoint(0.0, 0.0), Timestamp.now())

  //We never want to bind it to any lifecycle
  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    isCreated = true
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    mLocationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        val coordinates = locationResult?.locations?.map { loc -> Coordinate(loc) }
        Timber.d("Received ${coordinates?.size ?: 0} new coordinates")

        if (coordinates?.any() == true) {
          mCoordinateQueue.addAll(coordinates)
          mLastKnownLocation = coordinates.last()
        }
        this@TrackerService.tryPostCoordinates()
        this@TrackerService.fetchNewPhotos()
        this@TrackerService.tryPostPhotos()
      }
    }
    mServiceScope.launch {
      val currentTrip = mTripsRepository.getCurrentTripSnapshot()
      mCurrentTrip = currentTrip ?: mTripsRepository.createTrip()
      Timber.d("trip = $mCurrentTrip")
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    //TODO[AR]: Suddenly it started throwing intent is null??? intent should not be nullable
    if (intent == null) {
      val notification = createNotification()
      startForeground(1, notification)
      startLocationUpdates()
      return START_STICKY
    }
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
        stopSelf()
      }
    }

    return START_STICKY //calls onCreate and onStartCommand again when app is resurrected
  }

  override fun onDestroy() {
    super.onDestroy()
    isCreated = false
  }

  private fun createNotification(): Notification {
    val activityIntent = Intent(this, MainActivity::class.java).apply {
      putExtra("SENDER", "Notification")
    }
    val pendingIntent = PendingIntent.getActivity(
      this,
      0,
      activityIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

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
    if (::mCurrentTrip.isInitialized)
      mServiceScope.launch {
        withContext(Dispatchers.Default) {
          Timber.d("Sending ${mCoordinateQueue.size} coordinates to the DB.")
          mTripsRepository.addCoordinates(mCoordinateQueue.toTypedArray())
        }
        Timber.d("Cleaning coordinate queue.")
        mCoordinateQueue.clear()
      }
  }

  private fun fetchNewPhotos() {
    val newPhotos = mLocalPhotosRepository
      .getCameraPhotosSince(mLastPhotoUploadTime)
      .map {uri -> Picture(uri.toString(), mLastKnownLocation)}
    mPhotoQueue.addAll(newPhotos)
  }

  private fun tryPostPhotos() {
    if (mPhotoQueue.isEmpty()) {
      Timber.d("No photos to post.")
      return
    }
    if (::mCurrentTrip.isInitialized) {
      Timber.d("Posting ${mPhotoQueue.size} photos to trip ${mCurrentTrip.id}.")
      mServiceScope.launch {
        mTripsRepository.addPicturesToCurrentTrip(mPhotoQueue.toTypedArray())
        Timber.d("Cleaning photo queue")
        mPhotoQueue.clear()
      }
      mLastPhotoUploadTime = currentEpochTime
    }
  }
}
