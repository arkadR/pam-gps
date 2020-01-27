package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.coroutines.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    view.navigation_view.getHeaderView(0).nav_header_username.text =
      FirebaseAuth.getInstance().currentUser!!.email
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    navigation_view.setNavigationItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.logout ->{
          handleLogout()
          navigation_view.visibility = View.GONE
        }
      }
      true
    }
  }

  private fun handleLogout() {
    if (TrackerService.isRunning.value) {
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
    } else {
      FirebaseAuth.getInstance().signOut()
      findNavController().navigate(R.id.action_navigation_trip_to_navigation_login)
    }
  }
}