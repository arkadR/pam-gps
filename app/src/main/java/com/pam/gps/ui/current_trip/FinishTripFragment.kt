package com.pam.gps.ui.current_trip

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pam.gps.R
import com.pam.gps.extensions.withValidate
import com.pam.gps.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_finish_trip.*
import kotlinx.android.synthetic.main.fragment_finish_trip.view.*

class FinishTripFragment : Fragment() {

  private val viewModel by viewModels<CurrentTripViewModel>()
  private lateinit var picturesAdapter: PictureSelectAdapter

  private val saveButton: MenuItem
    get() = toolbar_finish_trip.menu.findItem(R.id.finish_trip_save_button)

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
        R.id.finish_trip_save_button -> { onSaveButton() }
        R.id.finish_trip_discard_button -> { onDiscardButton() }
        else -> throw RuntimeException("Menu item $item without handler clicked")
      }
    }

    viewModel.currentTrip.observe(viewLifecycleOwner) {
      if (it?.tripDetails?.pictures.isNullOrEmpty()) return@observe
      picturesAdapter.setData(it!!.tripDetails!!.pictures)
    }

    viewModel.requestCurrentTripUpdate()

    picturesAdapter = PictureSelectAdapter()

    view.pictures_recycler.apply {
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

    //TODO[AR]: Better validation, move to one place
    title_text.withValidate(title_layout, "Please enter the title of the trip") { title_text.text?.isEmpty() == false }
    title_text.addTextChangedListener { title_layout.error = null }
    title_text.addTextChangedListener { saveButton.isEnabled = title_text.text!!.isNotEmpty() }
  }

  private fun onDiscardButton(): Boolean {
    MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
      .setTitle("Discard this trip?")
      .setMessage("You are about to discard this trip and lose it forever. Are you sure you want to proceed?")
      .setPositiveButton("Yes") { _, _ -> this@FinishTripFragment.discardTrip() }
      .setNegativeButton("Cancel") { _, _ -> Unit }
      .create()
      .show()
    return false
  }


  private fun onSaveButton(): Boolean {
    hideKeyboard(requireContext(), requireView())
    saveTrip()
    findNavController().navigate(R.id.action_finishTripFragment_to_navigation_home)
    return true
  }

  private fun discardTrip() {
    viewModel.discardTrip()
    findNavController().navigate(R.id.action_finishTripFragment_to_navigation_home)
  }

  private fun saveTrip() {
    viewModel.saveTrip(
      title_text.text.toString(),
      picturesAdapter.imageUris.getOrElse(picturesAdapter.selectedPosition) { "" }
    )
  }
}
