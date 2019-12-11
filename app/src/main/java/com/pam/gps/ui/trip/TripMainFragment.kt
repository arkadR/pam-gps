package com.pam.gps.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_trip_main.*
import timber.log.Timber


class TripMainFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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

    val beh = BottomSheetBehavior.from(bottom_sheet)
    beh.state = BottomSheetBehavior.STATE_COLLAPSED
    Timber.d("opacity = ${beh.getScrimOpacity(coordinator, bottom_sheet)}")

/*    viewModel.tripDetails.observe(viewLifecycleOwner, Observer { tripDetails ->
      Timber.d("trip details = $tripDetails")
      mapFragment.getMapAsync { googleMap ->
        googleMap.addPath(tripDetails!!.coordinates)
        if (tripDetails.coordinates.isNotEmpty()) googleMap.centerOnPath(tripDetails.coordinates)
      }
    })*/
  }

}
