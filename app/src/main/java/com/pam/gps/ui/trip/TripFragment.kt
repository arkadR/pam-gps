package com.pam.gps.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.SupportMapFragment
import com.pam.gps.R
import com.pam.gps.extensions.addPath
import com.pam.gps.extensions.centerOnPath
import kotlinx.android.synthetic.main.fragment_trip.view.*
import timber.log.Timber


class TripFragment : Fragment() {
  private val args: TripFragmentArgs by navArgs()
  private val viewModel by viewModels<TripViewModel>()
  private val picturesAdapter = PicturesAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel.selectedTrip.value = args.trip
    val view = inflater.inflate(R.layout.fragment_trip, container, false)
    view.pictures_recycler.apply {
      adapter = picturesAdapter
      layoutManager = GridLayoutManager(context, 4)
    }
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.tripDetails.observe(viewLifecycleOwner, Observer {
      it?.let { picturesAdapter.setData(it.pictures) }
    })

    val mapFragment = childFragmentManager
      .findFragmentById(R.id.details_map) as SupportMapFragment

    viewModel.tripDetails.observe(viewLifecycleOwner, Observer { tripDetails ->
      Timber.d("trip details = $tripDetails")
      mapFragment.getMapAsync { googleMap ->
        googleMap.addPath(tripDetails!!.coordinates)
        googleMap.centerOnPath(tripDetails.coordinates)
      }
    })
  }
}
