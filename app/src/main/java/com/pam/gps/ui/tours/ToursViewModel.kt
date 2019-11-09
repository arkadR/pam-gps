package com.pam.gps.ui.tours

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pam.gps.extensions.asLiveData
import com.pam.gps.model.Tour
import com.pam.gps.repositories.ToursRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ToursViewModel : ViewModel() {
  val tours: LiveData<MutableList<Tour>>
  var toursRepository: ToursRepository = ToursRepository()

  init {
    tours = toursRepository.getTours("WHdXXyRmdXHNHlNuxb1P").asLiveData()
  }
}
