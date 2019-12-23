package com.pam.gps.ui.currentTrip

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.fragment_current_trip.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class CurrentTripFragment : Fragment() {

  companion object {
    fun newInstance() = CurrentTripFragment()
  }

  private val tripViewModel by viewModels<CurrentTripViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_current_trip, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    sign_out_button.setOnClickListener {
      FirebaseAuth.getInstance().signOut()
      findNavController().navigate(R.id.action_navigation_trip_to_navigation_login)
    }
    if (TrackerService.isRunning)
      fab.setImageResource(R.drawable.ic_stop_tracker)
    fab.setOnClickListener {
      when {
        TrackerService.isRunning -> {
          findNavController().navigate(R.id.action_navigation_trip_to_finishTripFragment)
          //stopService()
        }
        else -> startService()
      }
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

  private fun startService() {
    val startIntent = Intent(requireContext(), TrackerService::class.java)
    startIntent.action = TrackerService.START_SERVICE_CODE
    startForegroundService(requireContext(), startIntent)
    fab.setImageResource(R.drawable.ic_stop_tracker)
  }

  private fun stopService() {
    val stopIntent = Intent(requireContext(), TrackerService::class.java)
    stopIntent.action = TrackerService.STOP_SERVICE_CODE
    startForegroundService(requireContext(), stopIntent)
    fab.setImageResource(R.drawable.ic_start_tracker)
  }
}
