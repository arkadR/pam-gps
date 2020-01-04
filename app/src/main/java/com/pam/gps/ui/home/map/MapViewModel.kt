package com.pam.gps.ui.home.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pam.gps.model.LocalPicture
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.transform

@InternalCoroutinesApi
class MapViewModel : ViewModel() {

  private val allTripDetails = TripsRepository()
    .getAllTripsDetails()

  private val localPictures =
    allTripDetails
      .transform { allDetails ->
        allDetails.flatMap { details -> details.pictures }
          .map { pictureUri -> emit(LocalPicture.fromUri(pictureUri)) }
      }

  val mapMarkers =
    localPictures.transform { pic -> emit(MapMarker.fromPicture(pic)) }

  val tripPaths =
    allTripDetails
      .transform { allDetails ->
        emit(allDetails.map { details -> details.coordinates })
      }
      .asLiveData()

}
