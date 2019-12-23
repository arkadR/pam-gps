package com.pam.gps.ui.currentTrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import kotlinx.android.synthetic.main.finish_trip_fragment.view.*

class FinishTripFragment : Fragment() {

  private val viewModel by viewModels<CurrentTripViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.finish_trip_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenResumed {
      val picturesAdapter = PictureSelectAdapter(
        requireContext(),
        viewModel.currentTrip.await()?.tripDetails?.pictures ?: emptyList()
      )

      with(view.pictures_recycler) {
        adapter = picturesAdapter

        layoutManager = GridLayoutManager(requireContext(), 3)

        addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
          override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child: View? = findChildViewUnder(e.x, e.y)
            if (child != null) {
              val newSelectedIndex = getChildAdapterPosition(child)
              val oldSelectedIndex = picturesAdapter.selectedPosition
              picturesAdapter.selectedPosition = newSelectedIndex

              picturesAdapter.notifyItemChanged(oldSelectedIndex)
              picturesAdapter.notifyItemChanged(newSelectedIndex)
            }
            return false
          }
        })
      }
    }
  }
}
