package com.pam.gps.ui.currentTrip

import android.view.MotionEvent
import android.widget.ImageView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.ui.bindingAdapters.bindStorageUri

class PictureSelectViewHolder(private val imageView: ImageView) :
  RecyclerView.ViewHolder(imageView) {
  private var uri: String? = null
  fun bind(
    isActivated: Boolean,
    uri: String
  ) {
    this.uri = uri
    imageView.isActivated = isActivated
    imageView.bindStorageUri(uri)
  }

  fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
    object : ItemDetailsLookup.ItemDetails<String>() {
      override fun getSelectionKey(): String? = uri
      override fun getPosition(): Int = adapterPosition
      override fun inSelectionHotspot(e: MotionEvent): Boolean {
        return true
      }
    }
}
