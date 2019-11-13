package com.pam.gps.ui.trips

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pam.gps.R

class TripFragment : Fragment() {

  companion object {
    fun newInstance() = TripFragment()
  }

  private lateinit var viewModel: TripViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_trip, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(TripViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
