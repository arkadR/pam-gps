package com.pam.gps.ui.trip

import androidx.lifecycle.*
import com.pam.gps.model.Trip
import com.pam.gps.model.TripDetails
import com.pam.gps.repositories.TripsRepository

class TripViewModel : ViewModel() {
  private val tripsRepository = TripsRepository()
  val selectedTrip: MutableLiveData<Trip?> = MutableLiveData()
  val tripDetails: LiveData<TripDetails?> = selectedTrip.switchMap { trip ->
    trip?.details!!.let { path ->
      tripsRepository.getTripDetails(path).asLiveData()
    }
  }
}
