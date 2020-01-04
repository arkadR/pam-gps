package com.pam.gps.utils

import android.content.SharedPreferences
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocalPhotoCache(private val sharedPrefs: SharedPreferences) {

  suspend fun getLocalPathByRef(ref: String): String {
    if (isCached(ref))
      return getLocalPath(ref)

    val localPath = downloadFromFirebase(ref)
    cachePhoto(ref, localPath)
    return localPath
  }



  private fun isCached(photoRef: String): Boolean {
    return sharedPrefs.contains(photoRef)
  }

  private fun getLocalPath(photoRef: String): String {
    if (!isCached(photoRef))
      throw IllegalArgumentException("Photo with this reference is not cached!")

    return sharedPrefs.getString(photoRef, null)!!
  }

  private fun cachePhoto(photoRef: String, localPath: String) {
    if (isCached(photoRef))
      throw IllegalArgumentException("Photo with passed reference is already cached!")

    with(sharedPrefs.edit()) {
      putString(photoRef, localPath)
      commit()
    }
  }

  //downloads a picture from firebase to a local file and returns the path
  private suspend fun downloadFromFirebase(uri: String): String {
    Timber.d("Getting bitmap from $uri")
    return suspendCoroutine { cont ->
      val ref = FirebaseStorage.getInstance().getReference(uri);
      try {
        val localFile = File.createTempFile("Image", "bmp")
        Timber.d("Created a temp file: $localFile")
        ref.getFile(localFile)
          .addOnSuccessListener {
            Timber.d("Got the image!")
            cont.resume(localFile.absolutePath)
          }
          .addOnFailureListener { exc -> Timber.d(exc); cont.resumeWithException(exc) }
      } catch (exc: IOException) {
        Timber.d(exc)
        cont.resumeWithException(exc)
      }
    }
  }
}
