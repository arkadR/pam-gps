package com.pam.gps.model

import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Coordinate(val geoPoint: GeoPoint? = null, val timestamp: Timestamp? = null) {

  constructor(location: Location) : this(
    GeoPoint(location.latitude, location.longitude),
    Timestamp(location.time/1000, 0)
  )

  fun asLatLng(): LatLng? {
    return if (geoPoint != null && timestamp != null)
      LatLng(geoPoint.latitude, geoPoint.longitude) else null
  }
}