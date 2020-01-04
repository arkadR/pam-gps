package com.pam.gps.ui.home.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pam.gps.model.LocalPicture
import com.pam.gps.utils.LocalPhotoCache
import com.pam.gps.utils.PhotoLocationCache
import org.koin.core.KoinComponent
import org.koin.core.inject

class MapMarker(private var location: LatLng, private val pictureUri: String) : ClusterItem, KoinComponent {

  companion object {
    fun fromPicture(pic: LocalPicture): MapMarker {
      return MapMarker(pic.latLng!!, pic.uri)
    }
  }

  private val localPhotoCache: LocalPhotoCache by inject()
  private val locationCache: PhotoLocationCache by inject()

  var localPath: String? = null

  override fun getSnippet(): String = "Snippet"

  override fun getTitle(): String = "Title"

  override fun getPosition(): LatLng = location

  //TODO[AR]: remove that, create the object in a better way
  suspend fun prepareMarker() {
    localPath = localPhotoCache.getLocalPathByRef(pictureUri)
    location = locationCache.getLocationByRef(pictureUri) ?: location
  }
}