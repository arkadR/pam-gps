package com.pam.gps.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking

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

  val mapMarkers =
    TripsRepository()
      .getAllTripsDetails()
      .transform { allDetails ->
        emit (allDetails.flatMap { details -> details.coordinates }
          .map { coord -> MapMarker(coord) })
      }
      .asLiveData()
}
