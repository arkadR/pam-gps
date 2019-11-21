package com.pam.gps.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions
import com.pam.gps.R
import timber.log.Timber


class TripFragment : Fragment() {
  private val args: TripFragmentArgs by navArgs()
  private val viewModel by viewModels<TripViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel.selectedTrip.value = args.trip
    return inflater.inflate(R.layout.fragment_trip, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val mapFragment = childFragmentManager
      .findFragmentById(R.id.details_map) as SupportMapFragment

    viewModel.tripDetails.observe(viewLifecycleOwner, Observer { tripDetails ->
      Timber.d("trip details = $tripDetails")
      mapFragment.getMapAsync { maps ->
        maps.addPolyline(PolylineOptions().apply {
          if (tripDetails != null) add(*tripDetails.coordinates.map { it.asLatLng() }.toTypedArray())
        })
      }
    })
  }
}
