package com.pam.gps.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.pam.gps.extensions.asFlow
import com.pam.gps.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

open class TripsRepository  {

  private val instance = Firebase.firestore

  @ExperimentalCoroutinesApi
  fun getTrips(userId: String): Flow<List<Trip>> {
    return instance
      .collection("users")
      .document(userId)
      .collection("tours") //TODO[AR]: Change that to trips
      .asFlow()
      .flowOn(Dispatchers.IO)
      .filterNotNull()
      .mapNotNull { it.toObjects<Trip>() }
      .flowOn(Dispatchers.Default)
  }
}