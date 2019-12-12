package com.pam.gps.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_trip_details.view.*
import timber.log.Timber


class TripDetailsFragment : Fragment() {
  private val viewModel by activityViewModels<TripViewModel>()
  private val picturesAdapter = PicturesAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    Timber.d("Method called")
    val view = inflater.inflate(R.layout.fragment_trip_details, container, false)
    view.pictures_recycler.apply {
      adapter = picturesAdapter
      layoutManager = GridLayoutManager(context, 4)
    }
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.tripDetails.observe(viewLifecycleOwner, Observer {
      Timber.d("tripdetails = $it")
      it?.let { picturesAdapter.setData(it.pictures) }
    })

  }
}
