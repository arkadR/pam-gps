package com.pam.gps.ui.current_trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.CurrentTrip
import com.pam.gps.repositories.TripsRepository

class CurrentTripViewModel : ViewModel() {
  val repository = TripsRepository()

  val currentTrip: LiveData<CurrentTrip?> = repository.getCurrentTrip()
    .asLiveData(viewModelScope.coroutineContext)
}