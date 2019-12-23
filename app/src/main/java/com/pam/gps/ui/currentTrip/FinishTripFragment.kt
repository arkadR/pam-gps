package com.pam.gps.ui.currentTrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.pam.gps.R
import kotlinx.android.synthetic.main.finish_trip_fragment.view.*
import timber.log.Timber

class FinishTripFragment : Fragment() {

  private val viewModel by viewModels<CurrentTripViewModel>()
  private lateinit var adapter: PictureSelectAdapter
  private lateinit var selectionTracker: SelectionTracker<String>

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.finish_trip_fragment, container, false)
    adapter = PictureSelectAdapter(requireContext())
    view.pictures_recycler.adapter = adapter
    view.pictures_recycler.layoutManager = GridLayoutManager(requireContext(), 3)

    viewModel.images.observe(viewLifecycleOwner) {
      it?.let {
        adapter.imageUris = it
        adapter.notifyDataSetChanged()
      }
    }

    selectionTracker = SelectionTracker.Builder<String>(
      "my-selection-id",
      view.pictures_recycler,
      StringKeyProvider(adapter),
      StringItemLookup(view.pictures_recycler),
      StorageStrategy.createStringStorage()
    )
      .withOnItemActivatedListener { item, _ ->
        Timber.d(item.selectionKey)
        viewModel.selectedImageUri.value = item.selectionKey
        return@withOnItemActivatedListener true
      }
      .withSelectionPredicate(
        SingleNoDeselectSelectPredicate
      )
      .build()

    selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
      override fun onItemStateChanged(key: String, selected: Boolean) {
        if (!selectionTracker.hasSelection())
          selectionTracker.select(key)
      }
    })

    adapter.selectionPredicate = { s: String -> selectionTracker.isSelected(s) }

    savedInstanceState?.let {
      selectionTracker.onRestoreInstanceState(it)
    } ?: if (!selectionTracker.hasSelection() && adapter.imageUris.isNotEmpty()) {
      selectionTracker.select(adapter.imageUris[0])
    }
    return view
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    selectionTracker.onSaveInstanceState(outState)
  }
}
