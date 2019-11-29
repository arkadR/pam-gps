package com.pam.gps.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pam.gps.model.Coordinate

class MapMarker(private val m_position: LatLng) : ClusterItem {

  constructor(coord: Coordinate) : this(
    LatLng(
      coord.geoPoint!!.latitude,
      coord.geoPoint.longitude
    )
  )


  override fun getSnippet(): String = "Snippet"

  override fun getTitle(): String = "Title"

  override fun getPosition(): LatLng = m_position
}