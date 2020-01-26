package com.pam.gps.ui.home

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.view.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    view.navigation_view.getHeaderView(0).nav_header_username.text =
      FirebaseAuth.getInstance().currentUser!!.email
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    navigation_view.setNavigationItemSelectedListener { menuItem ->
      // Bottom Navigation Drawer menu item clicks
      when (menuItem.itemId) {
        R.id.logout -> {
          FirebaseAuth.getInstance().signOut()
          findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }
      }
      // Add code here to update the UI based on the item selected
      // For example, swap UI fragments here
      true
    }
  }
}