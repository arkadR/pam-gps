package com.pam.gps.ui.map

import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

  //TODO[ME] Commented because of FireStore refactor, need to fix
//  val mapMarkers = flow {
//    for (x in 0..10) {
//      for (y in 0..10) {
//        delay(1000)
//        emit(MapMarker(LatLng(x.toDouble(), y.toDouble())))
//      }
//    }
//  }.asLiveData()

//  val mapMarkers = TripsRepository()
//    .getCurrentTripDetails()
//    .mapNotNull {it?.coordinates?.map { coordinate -> MapMarker(coordinate) } }
//    .asLiveData()
}
