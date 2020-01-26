package com.pam.gps.model

import com.google.firebase.Timestamp

data class CurrentTrip(
  val id: String = "",
  val trip: Trip? = null,
  val tripDetails: TripDetails? = null,
  val startTime: Timestamp = Timestamp.now()
)