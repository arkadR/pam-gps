package com.pam.gps.ui.currentTrip

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class StringItemLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {
  override fun getItemDetails(e: MotionEvent): ItemDetails<String>? =
    recyclerView.findChildViewUnder(e.x, e.y)?.let {
      (recyclerView.getChildViewHolder(it) as PictureSelectViewHolder).getItemDetails()
    }
}