package com.pam.gps.model

data class CurrentTrip(
  val id: String = "",
  val trip: Trip? = null,
  val tripDetails: TripDetails? = null
)