package com.pam.gps.ui.current_trip

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_finish_trip.*
import kotlinx.android.synthetic.main.fragment_finish_trip.view.*

class FinishTripFragment : Fragment() {

  private val viewModel by viewModels<CurrentTripViewModel>()
  private lateinit var picturesAdapter: PictureSelectAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_finish_trip, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.currentTrip.observe(viewLifecycleOwner) {
      if (it?.tripDetails?.pictures.isNullOrEmpty()) return@observe
      picturesAdapter.setData(it!!.tripDetails!!.pictures)
    }

    val fab = activity?.fab
    fab?.visibility = View.GONE

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


  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.finish_trip_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.finish_trip_save_button -> {
        hideKeyboard(requireContext(), requireView())
        saveTrip()
        findNavController().navigate(R.id.action_finishTripFragment_to_navigation_home)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun saveTrip() {
    //TODO[ME] validate title
    viewModel.saveTrip(
      title_text.text.toString(),
      picturesAdapter.imageUris.getOrElse(picturesAdapter.selectedPosition) { "" }
    )
  }
}
