package com.pam.gps.ui.currentTrip

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.fragment_current_trip.*
import androidx.core.content.ContextCompat.startForegroundService
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
      findNavController().navigate(com.pam.gps.R.id.action_navigation_trip_to_navigation_login)
    }
    track_button.setOnClickListener {
      startService()
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

  private fun startService() {
    val serviceIntent = Intent(requireContext(), TrackerService::class.java)
    startForegroundService(requireContext(), serviceIntent)
  }

  private fun stopService1() {
//    val serviceIntent = Intent(requireContext(), TrackerService::class.java)
//    stopS
  }

}
