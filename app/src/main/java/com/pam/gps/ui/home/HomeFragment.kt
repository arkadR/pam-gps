package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.pam.gps.R
import com.pam.gps.ui.tours.ToursViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment() {

  @ExperimentalCoroutinesApi
  private val toursViewModel by viewModels<ToursViewModel>()

  private val viewAdapter: TripAdapter = TripAdapter()

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
    toursViewModel.tours.observe(viewLifecycleOwner, Observer { tours -> viewAdapter.setData(tours) })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }
}
