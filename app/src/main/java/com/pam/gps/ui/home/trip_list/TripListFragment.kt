package com.pam.gps.ui.home.trip_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.gps.R
import kotlinx.android.synthetic.main.fragment_trip_list.view.*

class TripListFragment : Fragment() {

  private val viewModel by viewModels<TripListViewModel>()

  private val viewAdapter: TripListAdapter = TripListAdapter()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_trip_list, container, false)
    root.recycler_view_trips.apply {
      layoutManager = LinearLayoutManager(this.context)
      adapter = viewAdapter
    }
    viewModel.trips.observe(viewLifecycleOwner, Observer { trips -> viewAdapter.setData(trips) })
    return root
  }
}
