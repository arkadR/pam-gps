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

  val distanceInKm: Double
  get() {
    if (coordinates.size < 2)
      throw Exception("Trip details with less than 2 coords should not exist.")
    val locations = coordinates
      .sortedBy { coord -> coord.timestamp }
      .map { coord -> coord.asLocation() }

    return (locations.dropLast(1) zip locations.drop(1))
      .fold(0.0, {acc, (prev, curr) -> acc + prev!!.distanceTo(curr) } ) / 1000
  }

  val durationInSeconds: Long
  get() {
    if (coordinates.size < 2)
      throw Exception("Trip details with less than 2 coords should not exist.")
    return coordinates
      .map {coord -> coord.timestamp}
      .sortedBy { timestamp -> timestamp }
      .let { it.last()!!.seconds - it.first()!!.seconds }
  }

  val paceInMinutesPerKm: Double
  get() = (durationInSeconds / 60) / distanceInKm
/*  val googleMapPath: PolylineOptions by lazy {
    PolylineOptions().apply {
      addCoordinates(coordinates)
    }
  }*/
}