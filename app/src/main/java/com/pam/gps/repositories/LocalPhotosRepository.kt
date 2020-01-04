package com.pam.gps.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.pam.gps.utils.LocalPhotoCache
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class LocalPhotosRepository(val context: Context): KoinComponent {

  private val cache: LocalPhotoCache by inject()

  @Suppress("DEPRECATION")
  @SuppressLint("InlinedApi")
  fun getCameraPhotosSince(epochTime: Long): List<Uri> {
    Timber.d("Attempting to read photos created after ${Date(epochTime)}")
    val uris = mutableListOf<Uri>()
    val projection = arrayOf(
      MediaStore.Images.Media.DATA,
      MediaStore.Images.Media.DATE_TAKEN,
      MediaStore.Images.Media.DESCRIPTION,
      MediaStore.Images.Media.BUCKET_ID)
    val selection = "${MediaStore.Images.Media.DATE_TAKEN} > ?"
    val selectionArgs = arrayOf(epochTime.toString())
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null,
        null
    )
    while (cursor?.moveToNext() == true) {
      val contentUri = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
      val dateTaken = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
      val description = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))
      val id = cursor.getLong(cursor.getColumnIndexOrThrow(projection[3]))
      uris.add(Uri.parse(contentUri))
//      Timber.d(dateTaken)
//      Timber.d(description)
//      Timber.d(id.toString())
    }
    cursor?.close()
    return uris;
  }
}