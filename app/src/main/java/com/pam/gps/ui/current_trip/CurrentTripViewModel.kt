package com.pam.gps.ui.current_trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pam.gps.model.CurrentTrip
import com.pam.gps.repositories.TripsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentTripViewModel : ViewModel() {


  private val mTripsRepository = TripsRepository()

  private val mCurrentTrip = MutableLiveData<CurrentTrip?>()
  val currentTrip: LiveData<CurrentTrip?> = mCurrentTrip

  fun saveTrip(title: String, thumbnailPath: String?) {
    GlobalScope.launch {
      mTripsRepository.finishTrip(title, thumbnailPath.orEmpty())
    }
  }

  fun discardTrip() {
    GlobalScope.launch {
      mTripsRepository.discardTrip()
    }
  }

  fun requestCurrentTripUpdate() {
    viewModelScope.launch {
      mCurrentTrip.value = mTripsRepository.getCurrentTripSnapshot()
    }
  }

}
