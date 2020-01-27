package com.pam.gps.ui.home.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.clustering.ClusterManager
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import com.pam.gps.extensions.centerOnPath
import com.pam.gps.ui.home.HomeFragmentDirections
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MapFragment : Fragment() {

  private val mapViewModel by viewModels<MapViewModel>()

  private lateinit var mClusterManager: ClusterManager<MapMarker>

  private lateinit var mapView: MapView

  private lateinit var mapLoadingJob: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mapView = MapView(requireContext())

    mapLoadingJob = lifecycleScope.launchWhenResumed {
      delay(500) //for animation to finish xD
      mapView.onCreate(savedInstanceState)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_map, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenCreated {
      mapLoadingJob.join()
      map_progress_bar.visibility = View.GONE
      map_frame.addView(
        mapView,
        FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.MATCH_PARENT
        )
      )
    }
  }




  override fun onResume() {
    super.onResume()
    lifecycleScope.launchWhenResumed {
      mapLoadingJob.join()
      mapView.onResume()
    }
    mapView.getMapAsync { googleMap ->
      googleMap.isMyLocationEnabled = true
      setUpClusterManager(googleMap)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    map_frame.removeAllViews()
  }

  private fun onItemClicked(marker: MapMarker) {
    findNavController().navigate(
      HomeFragmentDirections.actionNavigationHomeToTripFragmentMain(marker.tripDetailsId)
    )
  }

  private fun setUpClusterManager(googleMap: GoogleMap) {
    mClusterManager = ClusterManager(this.context, googleMap)
    mClusterManager.renderer = IconMarkerManagerRenderer(
      requireContext(), googleMap, mClusterManager, ::onItemClicked
    )
    googleMap.setOnCameraIdleListener(mClusterManager)
    googleMap.setOnMarkerClickListener(mClusterManager)
    mClusterManager.setAnimation(true)
    GlobalScope.launch {
      val markers = mapViewModel.mapMarkers
      markers.collect { marker ->
        marker.prepareMarker()
        mClusterManager.addItem(marker)
        launch(Dispatchers.Main) { mClusterManager.cluster() }
      }
    }
    mapViewModel.tripPaths.observe(viewLifecycleOwner, Observer { pathList ->
      for (path in pathList) {
        googleMap.addPath(path)
      }
      googleMap.centerOnPath(pathList.flatten());
    })
  }
}
