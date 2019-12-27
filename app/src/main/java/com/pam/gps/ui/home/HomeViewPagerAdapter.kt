package com.pam.gps.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pam.gps.ui.home.map.MapFragment
import com.pam.gps.ui.home.trip_list.TripListFragment

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
  override fun getItemCount(): Int = 2

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> TripListFragment()
      1 -> MapFragment()
      else -> throw IllegalStateException("No position other than 0 or 1 is valid")
    }
  }

  fun getTitleForPosition(position: Int): String {
    return when (position) {
      0 -> "Trips"
      1 -> "Map"
      else -> throw IllegalStateException("No position other than 0 or 1 is valid")
    }
  }
}