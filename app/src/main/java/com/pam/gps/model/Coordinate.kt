package com.pam.gps.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Coordinate(val geoPoint: GeoPoint? = null, val timestamp: Timestamp? = null) {

  constructor(location: Location) : this(
    GeoPoint(location.latitude, location.longitude),
    Timestamp(location.time / 1000, 0)
  )

  fun asLatLng(): LatLng {
    if (geoPoint == null) throw IllegalStateException("Invalid call to asLatLng")
    return LatLng(geoPoint.latitude, geoPoint.longitude)
  }

  fun asLocation(): Location {
    if (geoPoint == null) throw IllegalStateException("Invalid call to asLatLng")
    return Location("").apply {
      this.latitude = geoPoint.latitude
      this.longitude = geoPoint.longitude
    }
  }
}