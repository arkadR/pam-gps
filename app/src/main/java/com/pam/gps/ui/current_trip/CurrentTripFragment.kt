package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.pam.gps.MainActivity
import com.pam.gps.R
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
    tripViewModel.currentTrip
    (requireActivity() as MainActivity).setupFab()

    val adapter = CurrViewPagerAdapter(this)
    curr_view_pager.adapter = adapter
    curr_view_pager.isUserInputEnabled = false

    TabLayoutMediator(cur_tab_layout, curr_view_pager) { tab, position ->
      tab.text = adapter.getTitleForPosition(position)
    }.attach()

  }


}
