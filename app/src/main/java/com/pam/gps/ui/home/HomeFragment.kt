package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.pam.gps.R
import com.pam.gps.TrackerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val adpt = HomeViewPagerAdapter(this)

    home_view_pager.adapter = adpt
    home_view_pager.isUserInputEnabled = false

    TabLayoutMediator(home_tab_layout, home_view_pager) { tab, position ->
      tab.text = adpt.getTitleForPosition(position)
    }.attach()

    setupHomeFab()
  }

  private fun setupHomeFab() {
    val isRunning = TrackerService.isRunning


    requireActivity().fab.setImageResource(
      if (isRunning)
        R.drawable.ic_trip_pace else R.drawable.ic_start_tracker
    )

    requireActivity().fab.setOnClickListener {
      if (!isRunning) TrackerService.start(requireContext())

      findNavController()
        .navigate(R.id.action_global_navigation_current_trip)
    }
  }
}
