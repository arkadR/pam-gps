package com.pam.gps.ui.finish_trip

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.ui.binding_adapters.bindStorageUri

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
}
