package com.pam.gps.model

import com.google.firebase.Timestamp

data class TripDetails(
  val id: String = "",
  val title: String = "",
  val date: Timestamp = Timestamp.now(),
  val coordinates: List<Coordinate> = emptyList(),
  val access: List<User> = emptyList(),
  val pictures: List<Picture> = emptyList()
) {

  //TODO[AR]: Ideally those would be properties but there is no way to tag a property as not serializable for firestore
  fun distanceInKm(): Double {
    if (coordinates.size < 2)
      return 0.0
//      throw Exception("Trip details with less than 2 coords should not exist.")
    //TODO[ME] And if trip was just created? It crashes if you try to watch it in ViewModel

    val locations = coordinates
      .sortedBy { coord -> coord.timestamp }
      .map { coord -> coord.asLocation() }

    return (locations.dropLast(1) zip locations.drop(1))
      .fold(0.0, {acc, (prev, curr) -> acc + prev!!.distanceTo(curr) } ) / 1000
  }

  fun durationInSeconds(): Long {
    if (coordinates.size < 2)
      return 0
//      throw Exception("Trip details with less than 2 coords should not exist.")
//TODO[ME] Same thing
    return coordinates
      .map {coord -> coord.timestamp}
      .sortedBy { timestamp -> timestamp }
      .let { it.last()!!.seconds - it.first()!!.seconds }
  }

  fun paceInMinutesPerKm(): Double = if (distanceInKm() == 0.0) 0.0 else (durationInSeconds() / 60) / distanceInKm()
/*  val googleMapPath: PolylineOptions by lazy {
    PolylineOptions().apply {
      addCoordinates(coordinates)
    }
  }*/
}