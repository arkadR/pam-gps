package com.pam.gps.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.pam.gps.model.Coordinate

fun GoogleMap.addPath(coords: List<Coordinate>) {
  val polyLine = PolylineOptions().apply {
    this.addAll(coords
      .sortedBy { coord -> coord.timestamp }
      .map { coord -> coord.asLatLng() }
    )
  }
  this.addPolyline(polyLine)
}

fun GoogleMap.centerOnPath(coords: List<Coordinate>) {
  val latLngs = LatLngBounds.Builder().apply {
    coords.forEach { coord -> this.include(coord.asLatLng()) }
  }.build()
  val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngs, 100)
  this.moveCamera(cameraUpdate)
}