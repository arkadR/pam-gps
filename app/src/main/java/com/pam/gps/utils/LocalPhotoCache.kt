package com.pam.gps.utils

import android.content.SharedPreferences
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocalPhotoCache: KoinComponent {

  private val photosSharedPrefs: SharedPreferences by inject(named("local_photo_cache_prefs"))

  suspend fun getLocalPathByRef(ref: String): String {
    if (isCached(ref)) {
      val localPath = getLocalPath(ref)
      if (!File(localPath).exists())
        removeFromCache(ref)
      else
        return localPath
    }

    val localPath = downloadFromFirebase(ref)
    cachePhoto(ref, localPath)
    return localPath
  }

  private fun isCached(photoRef: String): Boolean {
    return photosSharedPrefs.contains(photoRef)
  }

  private fun getLocalPath(photoRef: String): String {
    if (!isCached(photoRef))
      throw IllegalArgumentException("Photo with this reference is not cached!")

    return photosSharedPrefs.getString(photoRef, null)!!
  }

  private fun cachePhoto(photoRef: String, localPath: String) {
    if (isCached(photoRef))
      throw IllegalArgumentException("Photo with passed reference is already cached!")

    with(photosSharedPrefs.edit()) {
      putString(photoRef, localPath)
      commit()
    }
  }

  private fun removeFromCache(photoRef: String) {
    with(photosSharedPrefs.edit()) {
      remove(photoRef)
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
