package com.pam.gps.ui.currentTrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.CurrentTrip
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CurrentTripViewModel : ViewModel() {


  private val mTripsRepository = TripsRepository()

  fun saveTrip(title: String, thumbnailPath: String?) {
    GlobalScope.launch {
      mTripsRepository.finishTrip(title, thumbnailPath.orEmpty())
    }
  }

  val currentTrip: Deferred<CurrentTrip?> =
    viewModelScope.async {
      return@async mTripsRepository.getCurrentTripSnapshot()
    }
}
