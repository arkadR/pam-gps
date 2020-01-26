package com.pam.gps.ui.home.trip_list.trip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.ui.binding_adapters.bindStorageUri

class SelectablePicturesAdapter :
  RecyclerView.Adapter<SelectablePicturesAdapter.PictureViewHolder>() {
  private var data: List<String> = emptyList()

  class PictureViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
    fun bind(storageUri: String) {
      imageView.bindStorageUri(storageUri)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
    val imageView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.selectable_picture, parent, false)
    return PictureViewHolder(imageView as ImageView)
  }

  override fun getItemCount(): Int = data.size

  override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
    holder.bind(data[position])
  }

  fun setData(newData: List<String>) {
    data = newData
    notifyDataSetChanged()
  }
}
