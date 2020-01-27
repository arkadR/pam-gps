package com.pam.gps.ui.home.trip_list.trip

import androidx.lifecycle.*
import com.pam.gps.model.TripDetails
import com.pam.gps.repositories.TripsRepository

class TripViewModel : ViewModel() {
  private val tripsRepository = TripsRepository()
  val selectedTrip: MutableLiveData<String?> = MutableLiveData()

  val tripDetails: LiveData<TripDetails?> = selectedTrip.switchMap { tripDetailsId ->
    tripDetailsId?.let {
      tripsRepository.getTripDetailsById(it).asLiveData(viewModelScope.coroutineContext)
    } ?: MutableLiveData()
  }
}
