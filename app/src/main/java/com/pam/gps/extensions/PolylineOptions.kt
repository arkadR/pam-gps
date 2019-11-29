package com.pam.gps.extensions

import com.google.android.gms.maps.model.PolylineOptions
import com.pam.gps.model.Coordinate

fun PolylineOptions.addCoordinates(coords: List<Coordinate>) {
  this.addAll(coords
    .sortedBy { coord -> coord.timestamp }
    .map { coord -> coord.asLatLng() }
  )
}