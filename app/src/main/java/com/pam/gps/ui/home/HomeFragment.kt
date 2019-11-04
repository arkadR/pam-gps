package com.pam.gps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.pam.gps.R
import com.pam.gps.ui.tours.ToursViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  @ExperimentalCoroutinesApi
  private lateinit var toursViewModel: ToursViewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    toursViewModel = ViewModelProviders.of(this).get(ToursViewModel::class.java)

    val root = inflater.inflate(R.layout.fragment_home, container, false)
    val textView: TextView = root.findViewById(R.id.text_home)
    toursViewModel.tours.observe(viewLifecycleOwner, Observer {
      textView.text = it[0].title
    })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    view.findViewById<View>(R.id.button_home).setOnClickListener {
      val action = HomeFragmentDirections
        .actionHomeFragmentToHomeSecondFragment("From HomeFragment")
      NavHostFragment.findNavController(this@HomeFragment)
        .navigate(action)
    }
  }
}
