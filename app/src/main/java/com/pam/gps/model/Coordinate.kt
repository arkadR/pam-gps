package com.pam.gps.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Coordinate(val geoPoint: GeoPoint? = null, val timestamp: Timestamp? = null) {
  fun asLatLng(): LatLng? {
    return if (geoPoint != null && timestamp != null)
      LatLng(geoPoint.latitude, geoPoint.longitude) else null
  }
}