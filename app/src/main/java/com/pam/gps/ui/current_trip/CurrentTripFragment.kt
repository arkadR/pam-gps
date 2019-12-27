package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_current_trip.*

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
      FirebaseAuth.getInstance().signOut()
      findNavController().navigate(R.id.action_navigation_trip_to_navigation_login)
    }

    val bab = requireActivity().bottom_appbar
    val fab = requireActivity().fab
    val isRunning = TrackerService.isRunning
    setupCurrentTripFab(fab, bab, isRunning)
  }

  private fun setupCurrentTripFab(
    fab: FloatingActionButton,
    bab: BottomAppBar,
    isRunning: Boolean
  ) {
    bab.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    fab.visibility = View.VISIBLE
    fab.setImageResource(if (isRunning) R.drawable.ic_stop_tracker else R.drawable.ic_start_tracker)
    fab.setOnClickListener {
      TrackerService.stop(requireContext())
      findNavController().navigate(R.id.action_navigation_trip_to_finishTripFragment)
    }
  }
}