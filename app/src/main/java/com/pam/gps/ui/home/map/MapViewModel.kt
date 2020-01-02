package com.pam.gps.ui.home.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.pam.gps.repositories.TripsRepository
import com.pam.gps.utils.downloadFromFirebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlin.random.Random

class MapViewModel : ViewModel() {

  private val allTripDetails = TripsRepository()
    .getAllTripsDetails()

  init {

  }
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
    allTripDetails
      .transform { allDetails ->
        emit (allDetails.flatMap { details -> details.pictures }
          .map { pictureUri -> MapMarker(
            LatLng(
              Random.nextDouble(0.0, 10.0),
              Random.nextDouble(0.0, 10.0)
            ), pictureUri)
          })
      }
      .asLiveData()

  val tripPaths =
    allTripDetails
      .transform { allDetails ->
        emit(allDetails.map { details -> details.coordinates })
      }
      .asLiveData()

}
