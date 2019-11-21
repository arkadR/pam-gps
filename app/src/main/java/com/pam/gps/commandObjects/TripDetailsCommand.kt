package com.pam.gps.commandObjects

import com.google.firebase.Timestamp
import com.pam.gps.model.Coordinate
import com.pam.gps.model.TripDetails
import com.pam.gps.model.User

data class TripDetailsCommand(
  val title: String = "",
  val date: Timestamp = Timestamp.now(),
  val coordinates: List<Coordinate> = emptyList(),
  val access: List<User> = emptyList()
) {
  fun toTripDetails(id: String): TripDetails {
    return TripDetails(id, title, date, coordinates, access)
  }
}