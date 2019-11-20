package com.pam.gps.ui.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.model.Trip
import com.pam.gps.repositories.TripsRepository

class TripsViewModel : ViewModel() {
  val trips: LiveData<List<Trip>>
  var tripsRepository: TripsRepository = TripsRepository()

  init {
    trips = FirebaseAuth.getInstance().currentUser?.uid?.let {
      tripsRepository
        .getTrips(it)
        .asLiveData()
    } ?: throw RuntimeException("Firestore current user null")
  }
}
