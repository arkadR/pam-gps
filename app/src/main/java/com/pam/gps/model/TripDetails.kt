package com.pam.gps.model

import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.Timestamp
import com.pam.gps.extensions.addCoordinates

data class TripDetails(
  val id: String,
  val title: String = "",
  val date: Timestamp = Timestamp.now(),
  val coordinates: List<Coordinate> = emptyList(),
  val access: List<User> = emptyList(),
  val pictures: List<String> = emptyList(),
  val googleMapPath: PolylineOptions = PolylineOptions().apply {
    addCoordinates(coordinates)
  }
)