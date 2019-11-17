package com.pam.gps.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapMarker(private val m_position: LatLng) : ClusterItem {

  override fun getSnippet(): String = "Snippet"

  override fun getTitle(): String = "Title"

  override fun getPosition(): LatLng = m_position
}