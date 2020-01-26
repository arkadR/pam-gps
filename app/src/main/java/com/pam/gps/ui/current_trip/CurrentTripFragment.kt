package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.MainActivity
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.fragment_current_trip.*
import kotlinx.coroutines.*

class CurrentTripFragment : Fragment() {

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
      (lifecycleScope + Dispatchers.IO).launch {
        TrackerService.stop(requireContext())
        while (isActive) {
          if (!TrackerService.isRunning.value) {
            FirebaseAuth.getInstance().signOut()
            withContext(Dispatchers.Main) {
              findNavController().navigate(R.id.action_navigation_trip_to_navigation_login)
            }
            cancel()
          }
        }
      }

    }
    (requireActivity() as MainActivity).setupFab()
  }
}
