package com.pam.gps.ui.home.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.LocalPicture
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.flow.transform

class MapViewModel : ViewModel() {

  private val allTripDetails = TripsRepository()
    .getAllTripsDetails()

  private val localPictures =
    allTripDetails
      .transform { allDetails ->
        allDetails.flatMap { details -> details.pictures }
          .map { picture -> emit(LocalPicture.fromStorageRef(picture.storageRef!!)) }
      }

  val mapMarkers =
    localPictures.transform { pic -> emit(MapMarker.fromPicture(pic)) }

  val tripPaths =
    allTripDetails
      .transform { allDetails ->
        emit(allDetails.map { details -> details.coordinates })
      }
      .asLiveData(viewModelScope.coroutineContext)

}
