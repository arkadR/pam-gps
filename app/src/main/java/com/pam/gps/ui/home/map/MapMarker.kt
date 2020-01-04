package com.pam.gps.ui.home.map

import android.media.ExifInterface
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pam.gps.model.Coordinate
import com.pam.gps.utils.LocalPhotoCache
import com.pam.gps.utils.downloadFromFirebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class MapMarker(private var m_position: LatLng, val pictureUri: String) : ClusterItem, KoinComponent {

  val cache: LocalPhotoCache by inject()
  var localPath: String? = null

  constructor(coord: Coordinate, pictureUri: String) : this(
    LatLng(
      coord.geoPoint!!.latitude,
      coord.geoPoint.longitude
    ), pictureUri
  )


  override fun getSnippet(): String = "Snippet"

  override fun getTitle(): String = "Title"

  override fun getPosition(): LatLng = m_position

  suspend fun prepareMarker() {
    localPath = cache.getLocalPathByRef(pictureUri)
    val exif = ExifInterface(localPath!!)
    val floatArr = FloatArray(2)
    if (exif.getLatLong(floatArr)) {
      m_position = LatLng(floatArr[0].toDouble(), floatArr[1].toDouble())
    }
  }
}