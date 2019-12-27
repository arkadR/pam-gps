package com.pam.gps.ui.home.trip_list.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.SupportMapFragment
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import com.pam.gps.extensions.centerOnPath
import timber.log.Timber


class TripMainFragment : Fragment() {
  private val args: TripMainFragmentArgs by navArgs()
  private val viewModel by activityViewModels<TripViewModel>()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.selectedTrip.value = args.trip
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_trip_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val mapFragment = childFragmentManager
      .findFragmentById(R.id.details_map) as SupportMapFragment

    viewModel.tripDetails.observe(viewLifecycleOwner, Observer { tripDetails ->
      Timber.d("trip details = $tripDetails")
      mapFragment.getMapAsync { googleMap ->
        googleMap.addPath(tripDetails!!.coordinates)
        if (tripDetails.coordinates.isNotEmpty()) googleMap.centerOnPath(tripDetails.coordinates)
      }
    })
  }

}
