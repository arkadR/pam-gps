package com.pam.gps.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.io.File
import java.io.IOError
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun hideKeyboard(context: Context, view: View): Unit {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
}

suspend fun downloadFromFirebase(uri: String): String {
  Timber.d("Getting bitmap from $uri")
  return suspendCoroutine { cont ->
    val ref = FirebaseStorage.getInstance().getReference(uri);
    try {
      val localFile = File.createTempFile("Image", "bmp");
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