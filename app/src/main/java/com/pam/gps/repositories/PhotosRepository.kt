package com.pam.gps.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pam.gps.model.CurrentTrip
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File

class PhotosRepository {

  private val storageRef = Firebase.storage.reference
  private val userId by lazyOf(
    FirebaseAuth.getInstance().currentUser?.uid ?: throw RuntimeException("userId not available")
  )
//  private val tripsRepository = TripsRepository()

  suspend fun addPhotoToTrip(currentTrip: CurrentTrip, photoUri: Uri): String {
    if (currentTrip.tripDetails == null) throw RuntimeException("Trip details null for $currentTrip")
    val path = "${userId}/${currentTrip.tripDetails.id}/${photoUri.lastPathSegment}"
    Timber.d("upload path is $path")
    try {
      storageRef.child(path).putFile(Uri.fromFile(File(photoUri.toString()))).await()
    } catch (e: Exception) {
      Timber.e(e)
    } //TODO[ME] Recover
    return path
//    tripsRepository.addPhotoToCurrentTrip(arrayOf(path))
  }
}