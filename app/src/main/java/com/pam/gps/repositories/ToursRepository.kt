package com.pam.gps.repositories

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pam.gps.extensions.toFlow
import com.pam.gps.model.Tour
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

open class ToursRepository  {
  private val instance = Firebase.firestore

  @ExperimentalCoroutinesApi
  fun getTours(userId: String): Flow<MutableList<Tour>> {
    return instance
      .collection("users")
      .document(userId)
      .collection("tours")
      .toFlow()
      .filterNotNull()
      .mapNotNull { it.toObjects(Tour::class.java) }
  }
}