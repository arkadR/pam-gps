package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.gps.R
import com.pam.gps.ui.trips.TripsViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class HomeFragment : Fragment() {

  @ExperimentalCoroutinesApi
  private val toursViewModel by viewModels<TripsViewModel>()

  private val viewAdapter: TripListAdapter = TripListAdapter()

  @ExperimentalCoroutinesApi
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_home, container, false)
    root.recycler_view_trips.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(this.context)
      adapter = viewAdapter
    }
    toursViewModel.trips.observe(viewLifecycleOwner, Observer { tours -> viewAdapter.setData(tours) })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }
}
