package com.pam.gps.ui.currentTrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_current_trip.*

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
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

}
