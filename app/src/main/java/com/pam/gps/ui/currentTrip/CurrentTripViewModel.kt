package com.pam.gps.ui.currentTrip

import androidx.lifecycle.*
import com.pam.gps.model.CurrentTrip
import com.pam.gps.repositories.TripsRepository

class CurrentTripViewModel : ViewModel() {
  private val mTripsRepository = TripsRepository()

  val currentTrip: LiveData<CurrentTrip?> = mTripsRepository.getCurrentTrip().asLiveData()
  val selectedImageUri: MutableLiveData<String?> = MutableLiveData()
  val images: LiveData<List<String>?> = currentTrip.map {
    it?.tripDetails?.pictures
  }
}
