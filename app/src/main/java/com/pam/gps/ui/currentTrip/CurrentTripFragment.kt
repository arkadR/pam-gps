package com.pam.gps.ui.currentTrip

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pam.gps.R

class CurrentTripFragment : Fragment() {

  companion object {
    fun newInstance() = CurrentTripFragment()
  }

  private lateinit var viewModel: CurrentTripViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_current_trip, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(CurrentTripViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
