package com.pam.gps.ui.currentTrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.CurrentTrip
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class CurrentTripViewModel : ViewModel() {
  private val mTripsRepository = TripsRepository()

  val currentTrip: Deferred<CurrentTrip?> =
    viewModelScope.async {
      return@async mTripsRepository.getCurrentTripSnapshot()
    }
}
