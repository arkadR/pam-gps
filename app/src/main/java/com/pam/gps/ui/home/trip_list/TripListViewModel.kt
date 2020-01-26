package com.pam.gps.ui.home.trip_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.Trip
import com.pam.gps.repositories.TripsRepository

class TripListViewModel : ViewModel() {
  val trips: LiveData<List<Trip>>
  var tripsRepository: TripsRepository = TripsRepository()

  init {
    trips = tripsRepository.getTrips().asLiveData(viewModelScope.coroutineContext)
  }
}
