package com.pam.gps.ui.home.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.LocalPicture
import com.pam.gps.model.Picture
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class MapViewModel : ViewModel() {

  private val allTripDetails = TripsRepository()
    .getAllTripsDetails()

  val mapMarkers =
  allTripDetails
    .transform { allDetails ->
      allDetails
        .flatMap { details -> details.pictures.map {pic ->
          PictureData(details.title, pic, tripDetailsId = details.id)} }
        .map { obj -> emit(
          PictureData(obj.title, obj.picture, LocalPicture.fromStorageRef(obj.picture.storageRef!!), tripDetailsId = obj.tripDetailsId)) }
    }
    .map { obj -> MapMarker(obj.picture.coord!!.asLatLng(), obj.storageRef!!.uri, obj.title, obj.tripDetailsId!!) }

  val tripPaths =
    allTripDetails
      .transform { allDetails ->
        emit(allDetails.map { details -> details.coordinates })
      }
      .asLiveData(viewModelScope.coroutineContext)

}

data class PictureData(
  val title: String,
  val picture: Picture,
  val storageRef: LocalPicture? = null,
  val tripDetailsId: String? = null,
  val tripId: String? = null)