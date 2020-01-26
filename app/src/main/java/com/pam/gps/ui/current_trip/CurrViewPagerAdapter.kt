package com.pam.gps.ui.current_trip

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CurrViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
  override fun getItemCount() = 2

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> CurrentTripMapFragment()
      1 -> CurrentTripPhotosFragment()
      else -> throw IllegalArgumentException()
    }
  }

  fun getTitleForPosition(position: Int): String {
    return when (position) {
      0 -> "Map"
      1 -> "Photos"
      else -> throw IllegalArgumentException()
    }
  }
}