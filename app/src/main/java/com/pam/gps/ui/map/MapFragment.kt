package com.pam.gps.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    fun newInstance() = MapFragment()
  }

  private val mapViewModel by viewModels<MapViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    var view = inflater.inflate(R.layout.fragment_map, container, false)
    view.mapView.onCreate(savedInstanceState)
    view.mapView.getMapAsync(this);
    return view;
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }


  override fun onMapReady(googleMap: GoogleMap) {
    googleMap.isMyLocationEnabled = true
    mapView.onResume()
  }

}
