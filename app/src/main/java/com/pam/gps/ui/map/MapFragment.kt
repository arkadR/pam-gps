package com.pam.gps.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MapFragment : Fragment() {

  private val mapViewModel by viewModels<MapViewModel>()

  private lateinit var mClusterManager: ClusterManager<MapMarker>

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_map, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    mapView.onCreate(savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
    mapView.getMapAsync { googleMap ->
      googleMap.isMyLocationEnabled = true
      setUpClusterManager(googleMap)
      mapView.onResume()
    }
  }

  private fun setUpClusterManager(googleMap: GoogleMap) {
    mClusterManager = ClusterManager(this.context, googleMap)
    googleMap.setOnCameraIdleListener(mClusterManager)
    googleMap.setOnMarkerClickListener(mClusterManager)
    mClusterManager.setAnimation(true)
    //TODO[AR] Change markers to photos, draw path from coords
    mapViewModel.mapMarkers.observe(viewLifecycleOwner, Observer { markerList ->
      for (mapMarker in markerList) {
        mClusterManager.addItem(mapMarker)
      }
      mClusterManager.cluster()
    })
    mapViewModel.tripPaths.observe(viewLifecycleOwner, Observer { pathList ->
      for (path in pathList) {
        googleMap.addPath(path)
      }
    })
  }
}
