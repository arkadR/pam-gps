package com.pam.gps.ui.currentTrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth

import com.pam.gps.R
import com.pam.gps.extensions.clicks
import kotlinx.android.synthetic.main.fragment_current_trip.*
import kotlinx.coroutines.flow.collect

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
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

}
