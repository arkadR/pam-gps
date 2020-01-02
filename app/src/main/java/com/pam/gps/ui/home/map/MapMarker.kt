package com.pam.gps.ui.home.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pam.gps.model.Coordinate
import com.pam.gps.utils.downloadFromFirebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapMarker(private val m_position: LatLng, val pictureUri: String) : ClusterItem {

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
}