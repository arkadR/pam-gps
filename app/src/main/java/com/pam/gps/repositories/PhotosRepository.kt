package com.pam.gps.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pam.gps.model.Trip
import com.pam.gps.model.TripDetails
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class PhotosRepository {

  private val storageRef = Firebase.storage.reference
  private val userId by lazyOf(
    FirebaseAuth.getInstance().currentUser?.uid ?: throw RuntimeException("userId not available")
  )
  private val tripsRepository = TripsRepository()

  fun addPhotoToTrip(trip: Trip, tripDetails: TripDetails, photoUri: Uri)  = runBlocking {
    val path = "${userId}/${trip.id}/${photoUri.lastPathSegment}"
    val uploadTask = storageRef.child(path).putFile(Uri.fromFile(File(photoUri.toString()))).apply {
      addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val downloadUri = task.result
          launch {
            tripsRepository.addPhotoToTripDetails(tripDetails, arrayOf(path))
          }
        }
        else {
          //TODO[AR}: Then what?
        }
      }
    }
  }
}