package com.pam.gps.ui.currentTrip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import timber.log.Timber

class PictureSelectAdapter(
  context: Context,
  var imageUris: List<String>,
  var selectedPosition: Int = 0
) : RecyclerView.Adapter<PictureSelectViewHolder>() {
  private val inflater = LayoutInflater.from(context)


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureSelectViewHolder =
    PictureSelectViewHolder(inflater.inflate(R.layout.picture, parent, false) as ImageView)

  override fun getItemCount(): Int = imageUris.count()

  override fun onBindViewHolder(holder: PictureSelectViewHolder, position: Int) {
    val uri = imageUris[position]
    Timber.d("binding position $position, selected postion = $selectedPosition")
    holder.bind(position == selectedPosition, uri)
  }
}