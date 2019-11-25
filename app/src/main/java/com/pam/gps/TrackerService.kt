package com.pam.gps

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.location.Location
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.pam.gps.model.Coordinate
import com.pam.gps.model.TripDetails
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.lang.Exception

@InternalCoroutinesApi
class TrackerService : Service() {

  private lateinit var mFusedLocationClient: FusedLocationProviderClient

  private lateinit var mLocationRequest: LocationRequest
  private lateinit var mLocationCallback: LocationCallback

  private lateinit var mCurrentTripDetails: TripDetails

  private val mTripsRepository = TripsRepository()

  private val mHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    Timber.d(throwable)
  }

  private val mServiceJob = Job()
  private val mServiceScope = CoroutineScope(Dispatchers.Main + mServiceJob)

  private val mCoordQueue = mutableListOf<Coordinate>()

  //We never want to bind it to any lifecycle
  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    mLocationRequest = LocationRequest.create().apply {
      priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
      maxWaitTime = 60 * 60 * 1000
      fastestInterval = 30 * 1000
      interval = 5 * 60 * 1000
    }
    mLocationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        val coords = locationResult?.locations?.map { loc -> Coordinate(loc) }

        if (coords != null) {

          mCoordQueue.addAll(coords)

          if (::mCurrentTripDetails.isInitialized)
            mServiceScope.launch {
              async {
                mTripsRepository.addCoordinates(mCurrentTripDetails, mCoordQueue.toTypedArray())
              }.await()
              mCoordQueue.clear()
            }
        }
      }
    }
    mServiceScope.launch {
       mTripsRepository.getCurrentTripDetails().collect { list ->
        mCurrentTripDetails = when {
          list.isEmpty() -> {
            Timber.d("No active trip received from FireBase")
            val (_, tripDetails) = mTripsRepository.createTrip()
            tripDetails
          }
          list.size > 1 -> //TODO[AR]: Think if this can occur and handle it better
            throw Exception("Too many active trips")
          else -> list.first()
        }
      }
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val pendingIntent = Intent(this, MainActivity::class.java).let {
      PendingIntent.getActivity(this, 0, it, 0)
    }
    val notification = NotificationCompat
      .Builder(this, getString(R.string.notification_channel_id))
      .setContentTitle("Tracker Title")
      .setContentText("Content Text")
      .setContentIntent(pendingIntent)
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setSmallIcon(R.drawable.ic_notification_icon)
//      .addAction()
      .build()

    startForeground(1, notification)
    startLocationUpdates()

    return START_STICKY //calls onCreate and onStartCommand again when app is resurrected
  }

  override fun onDestroy() {
    super.onDestroy()
    mFusedLocationClient.removeLocationUpdates(mLocationCallback)
  }

  private fun startLocationUpdates() {
    mFusedLocationClient.requestLocationUpdates(
      mLocationRequest,
      mLocationCallback,
      mainLooper
    )
  }
}
