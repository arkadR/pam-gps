package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pam.gps.MainActivity
import com.pam.gps.R
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

    val adapter = HomeViewPagerAdapter(this)

    home_view_pager.adapter = adapter
    home_view_pager.isUserInputEnabled = false

    TabLayoutMediator(home_tab_layout, home_view_pager) { tab, position ->
      tab.text = adapter.getTitleForPosition(position)
    }.attach()

    (requireActivity() as MainActivity).setupFab()
  }
}
