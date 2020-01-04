package com.pam.gps.utils

import android.content.SharedPreferences
import android.media.ExifInterface
import com.google.android.gms.maps.model.LatLng
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class PhotoLocationCache: KoinComponent {

  private val locationSharedPrefs: SharedPreferences by inject(named("local_photo_cache_prefs"))

  private val localPhotosCache: LocalPhotoCache by inject()
  
  suspend fun getLocationByRef(ref: String): LatLng? {
    if (isCached(ref))
      return getLocation(ref)

    val localPath = localPhotosCache.getLocalPathByRef(ref)
    val location = readLocationFromFile(localPath) ?: return null

    cacheLocation(ref, location)
    return location
  }

  private fun readLocationFromFile(localPath: String): LatLng? {
    val exif = ExifInterface(localPath)
    val floatArr = FloatArray(2)
    return if (exif.getLatLong(floatArr))
      LatLng(floatArr[0].toDouble(), floatArr[1].toDouble())
    else null
  }


  private fun isCached(photoRef: String): Boolean {
    return locationSharedPrefs.contains("${photoRef}_lat")
  }

  private fun getLocation(photoRef: String): LatLng {
    if (!isCached(photoRef))
      throw IllegalArgumentException("Location with this reference is not cached!")

    val lat = locationSharedPrefs.getString("${photoRef}_lat", null)!!
    val lng = locationSharedPrefs.getString("${photoRef}_lng", null)!!
    return LatLng(lat.toDouble(), lng.toDouble())
  }

  private fun cacheLocation(photoRef: String, location: LatLng) {
    if (isCached(photoRef))
      throw IllegalArgumentException("Location with passed reference is already cached!")

    with(locationSharedPrefs.edit()) {
      putFloat("${photoRef}_lat", location.latitude.toFloat())
      putFloat("${photoRef}_lng", location.latitude.toFloat())
      commit()
    }
  }
}