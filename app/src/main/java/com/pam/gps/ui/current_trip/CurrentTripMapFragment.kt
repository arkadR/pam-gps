package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import com.pam.gps.extensions.centerOnPath
import com.pam.gps.extensions.withDecimalPlaces
import kotlinx.android.synthetic.main.fragment_current_trip_map.*
import kotlinx.android.synthetic.main.fragment_current_trip_map.view.*
import kotlinx.android.synthetic.main.fragment_trip_details.distance_text
import kotlinx.android.synthetic.main.fragment_trip_details.duration_text
import kotlinx.android.synthetic.main.fragment_trip_details.pace_text
import timber.log.Timber

class CurrentTripMapFragment : Fragment() {

  val currentTripViewModel by activityViewModels<CurrentTripViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_current_trip_map, container, false)
    view.curr_map_fragment.onCreate(savedInstanceState)
    return view
  }

  override fun onStart() {
    curr_map_fragment.onStart()
    super.onStart()
  }

  override fun onResume() {
    curr_map_fragment.onResume()
    super.onResume()
    currentTripViewModel.currentTrip.observe(viewLifecycleOwner) {
      if (it?.tripDetails == null) return@observe
      distance_text.text = it.tripDetails.distanceInKm().withDecimalPlaces(2) + " km"
      duration_text.text = (it.tripDetails.durationInSeconds()).toString() + "sec"
      pace_text.text = it.tripDetails.paceInMinutesPerKm().withDecimalPlaces(2) + "min/km"
    }
    curr_map_fragment.getMapAsync { maps ->
      currentTripViewModel.currentTrip.observe(viewLifecycleOwner) { trip ->
        if (trip?.tripDetails == null) return@observe
        Timber.d("coords: ${trip.tripDetails.coordinates.size}")
        maps.centerOnPath(trip.tripDetails.coordinates)
        maps.addPath(trip.tripDetails.coordinates)
      }
    }
  }

  override fun onPause() {
    curr_map_fragment.onPause()
    super.onPause()
  }

  override fun onStop() {
    curr_map_fragment.onStop()
    super.onStop()
  }

  override fun onDestroy() {
    //[ME] documentation says I should call this, but it just produces NPE...
//    curr_map_fragment.onDestroy()
    super.onDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    curr_map_fragment.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onLowMemory() {
    curr_map_fragment.onLowMemory()
    super.onLowMemory()
  }
}
