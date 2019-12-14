package com.pam.gps.model

import com.google.firebase.Timestamp

data class TripDetails(
  val id: String = "",
  val title: String = "",
  val date: Timestamp = Timestamp.now(),
  val coordinates: List<Coordinate> = emptyList(),
  val access: List<User> = emptyList(),
  val pictures: List<String> = emptyList()
) {

/*  val googleMapPath: PolylineOptions by lazy {
    PolylineOptions().apply {
      addCoordinates(coordinates)
    }
  }*/
}