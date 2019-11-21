package com.pam.gps.commandObjects

import com.google.firebase.Timestamp
import com.pam.gps.model.Trip

data class TripCommand(
  val title: String = "",
  val date: Timestamp? = null,
  val details: String = "",
  val finished: Boolean = false
) {
  fun toTrip(id: String): Trip {
    return Trip(id, title, date, details, finished)
  }
}