package com.pam.gps.ui.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pam.gps.model.Trip
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TripsViewModel : ViewModel() {
  val trips: LiveData<List<Trip>>
  var tripsRepository: TripsRepository = TripsRepository()

  init {
    trips = tripsRepository
      .getTrips("WHdXXyRmdXHNHlNuxb1P")
      .asLiveData()
  }
}
