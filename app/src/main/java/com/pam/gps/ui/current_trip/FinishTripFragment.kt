package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_finish_trip.*
import kotlinx.android.synthetic.main.fragment_finish_trip.view.*

class FinishTripFragment : Fragment() {

  private val viewModel by viewModels<CurrentTripViewModel>()
  private lateinit var picturesAdapter: PictureSelectAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_finish_trip, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    toolbar_finish_trip.inflateMenu(R.menu.finish_trip_menu)
    toolbar_finish_trip.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.finish_trip_save_button -> onSaveButton()
        else -> throw RuntimeException("Menu item $item without handler clicked")
      }
    }

    viewModel.currentTrip.observe(viewLifecycleOwner) {
      if (it?.tripDetails?.pictures.isNullOrEmpty()) return@observe
      picturesAdapter.setData(it!!.tripDetails!!.pictures)
    }

    viewModel.requestCurrentTripUpdate()

    picturesAdapter = PictureSelectAdapter()

    with(view.pictures_recycler) {
      adapter = picturesAdapter
      layoutManager = GridLayoutManager(requireContext(), 3)

      addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
          val child: View = findChildViewUnder(e.x, e.y) ?: return false

          val newSelectedIndex = getChildAdapterPosition(child)
          val oldSelectedIndex = picturesAdapter.selectedPosition

          picturesAdapter.selectedPosition = newSelectedIndex
          picturesAdapter.notifyItemChanged(oldSelectedIndex)
          picturesAdapter.notifyItemChanged(newSelectedIndex)

          return false
        }
      })
    }
  }

  fun onSaveButton(): Boolean {
    hideKeyboard(requireContext(), requireView())
    saveTrip()
    findNavController().navigate(R.id.action_finishTripFragment_to_navigation_home)
    return true
  }

  private fun saveTrip() {
    //TODO[ME] validate title
    viewModel.saveTrip(
      title_text.text.toString(),
      picturesAdapter.imageUris.getOrElse(picturesAdapter.selectedPosition) { "" }
    )
  }
}
