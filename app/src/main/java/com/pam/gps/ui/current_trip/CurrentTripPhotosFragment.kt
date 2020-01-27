package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_current_trip_photos.*
import timber.log.Timber


class CurrentTripPhotosFragment : Fragment() {

  private val currentTripViewModel by activityViewModels<CurrentTripViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_current_trip_photos, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val adapter = PicturesAdapter()
    recycler_view_photos.adapter = adapter
    recycler_view_photos.layoutManager = GridLayoutManager(requireContext(), 4)

    currentTripViewModel.currentTrip.observe(viewLifecycleOwner) {
      if (it?.tripDetails == null) return@observe
      Timber.d("photos: ${it.tripDetails.pictures.size}")
      adapter.setData(it.tripDetails.pictures.mapNotNull { pic -> pic.storageRef })
    }
  }
}
