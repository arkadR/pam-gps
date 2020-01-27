package com.pam.gps.ui.home.trip_list.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.MapView
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import com.pam.gps.extensions.centerOnPath
import kotlinx.android.synthetic.main.fragment_trip_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import timber.log.Timber


class TripMainFragment : Fragment() {
  private val args: TripMainFragmentArgs by navArgs()
  private val viewModel by activityViewModels<TripViewModel>()

  private lateinit var mapView: MapView
  private lateinit var mapLoadingJob: Job


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.selectedTrip.value = args.tripDetailsId

    mapView = MapView(requireContext())
    mapLoadingJob = lifecycleScope.launchWhenResumed {
      delay(500)
      mapView.onCreate(savedInstanceState)
    }
  }


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_trip_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    toolbar_trip.setNavigationOnClickListener {
      findNavController().navigateUp()
    }
    viewModel.tripDetails.observe(viewLifecycleOwner) { td ->
      toolbar_trip.title = td?.title ?: ""
    }
  }

  override fun onResume() {
    super.onResume()
    lifecycleScope.launchWhenResumed {
      mapLoadingJob.join()
      trip_main_progress_bar.visibility = View.GONE
      details_map_view.addView(
        mapView,
        FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.MATCH_PARENT
        )
      )
      viewModel.tripDetails.observe(viewLifecycleOwner, Observer { tripDetails ->
        Timber.d("trip details = $tripDetails")
        mapView.getMapAsync { googleMap ->
          googleMap.addPath(tripDetails!!.coordinates)
          if (tripDetails.coordinates.isNotEmpty()) googleMap.centerOnPath(tripDetails.coordinates)
        }
      })
      mapView.onResume()
    }
  }
}
