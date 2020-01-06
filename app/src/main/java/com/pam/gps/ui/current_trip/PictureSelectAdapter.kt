package com.pam.gps.ui.current_trip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import timber.log.Timber

class PictureSelectAdapter(
  var imageUris: List<String> = emptyList(),
  var selectedPosition: Int = 0
) : RecyclerView.Adapter<PictureSelectViewHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureSelectViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.picture, parent, false) as ImageView
    return PictureSelectViewHolder(view)
  }

  override fun getItemCount(): Int = imageUris.count()

  override fun onBindViewHolder(holder: PictureSelectViewHolder, position: Int) {
    val uri = imageUris[position]
    Timber.d("binding position $position, selected position = $selectedPosition")
    holder.bind(position == selectedPosition, uri)
  }

  fun setData(imageUris: List<String>) {
    this.imageUris = imageUris
    notifyDataSetChanged()
  }
}