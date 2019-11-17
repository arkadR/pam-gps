package com.pam.gps.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager

import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

@InternalCoroutinesApi
class MapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    fun newInstance() = MapFragment()
  }

  private val mapViewModel by viewModels<MapViewModel>()

  private lateinit var mClusterManager: ClusterManager<MapMarker>

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_map, container, false).apply {
      this.mapView.onCreate(savedInstanceState)
      this.mapView.getMapAsync(this@MapFragment)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

  override fun onMapReady(googleMap: GoogleMap) {
    googleMap.isMyLocationEnabled = true
    setUpClusterManager(googleMap)
    mapView.onResume()
  }

  private fun setUpClusterManager(googleMap: GoogleMap) {
    mClusterManager = ClusterManager(this.context, googleMap)
    googleMap.setOnCameraIdleListener(mClusterManager)
    googleMap.setOnMarkerClickListener(mClusterManager)
    mClusterManager.setAnimation(true)
    mapViewModel.mapMarkers.observe(this, Observer {
        marker -> mClusterManager.addItem(marker)
        mClusterManager.cluster()
    })
  }
}
