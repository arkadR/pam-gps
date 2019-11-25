package com.pam.gps.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class MapViewModel : ViewModel() {

//  val mapMarkers = flow {
//    for (x in 0..10) {
//      for (y in 0..10) {
//        delay(1000)
//        emit(MapMarker(LatLng(x.toDouble(), y.toDouble())))
//      }
//    }
//  }.asLiveData()

  @InternalCoroutinesApi
  val mapMarkers = TripsRepository()
    .getCurrentTripDetails()
    .mapNotNull { list -> if (list.isNotEmpty()) list.first().coordinates else emptyList() }
    .mapNotNull { coordList -> coordList.map { coord -> MapMarker(coord) } }
    .asLiveData()

}
