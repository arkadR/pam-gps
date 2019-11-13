package com.pam.gps.ui.tours

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pam.gps.model.Tour
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TripsViewModel : ViewModel() {
  val trips: LiveData<List<Tour>>
  var tripsRepository: TripsRepository = TripsRepository()

  init {
    trips = tripsRepository
      .getTrips("WHdXXyRmdXHNHlNuxb1P")
      .asLiveData()
  }
}
