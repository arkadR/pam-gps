package com.pam.gps.model

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.pam.gps.utils.LocalPhotoCache
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.random.Random

data class Picture(val uri: String? = null, val coord: Coordinate? = null, var storageRef: String? = null)

class LocalPicture(val uri: String, val latLng: LatLng?, val localPath: String) {

  companion object: KoinComponent {
    private val cache: LocalPhotoCache by inject()
    suspend fun fromStorageRef(uri: String): LocalPicture {
      return LocalPicture(uri, LatLng(Random.nextDouble()*10, Random.nextDouble()*10), cache.getLocalPathByRef(uri))
    }
  }
}