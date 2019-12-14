package com.pam.gps.commandObjects

import com.google.firebase.Timestamp
import com.pam.gps.model.Trip

data class TripCommand(
  val id: String = "",
  val title: String = "",
  val date: Timestamp = Timestamp.now(),
  val details: String = "",
  val finished: Boolean = false,
  val picture: String = ""
) {
  fun toTrip(id: String): Trip {
    return Trip(id, title, date, details, finished, picture)
  }
}