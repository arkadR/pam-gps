package com.pam.gps.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.pam.gps.model.Coordinate

fun GoogleMap.addPath(coordinates: List<Coordinate>) {
  val polyLine = PolylineOptions().apply {
    this.addAll(coordinates
      .sortedBy { coordinate -> coordinate.timestamp }
      .map { coordinate -> coordinate.asLatLng() }
    )
  }
  this.addPolyline(polyLine)
}

fun GoogleMap.centerOnPath(coordinates: List<Coordinate>) {
  val latLngBounds = LatLngBounds.Builder().apply {
    coordinates.forEach { coordinate -> this.include(coordinate.asLatLng()) }
  }.build()
  val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)
  this.moveCamera(cameraUpdate)
}