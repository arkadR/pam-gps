package com.pam.gps.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.pam.gps.BR
import com.pam.gps.R
import com.pam.gps.model.Trip

class TripListAdapter : RecyclerView.Adapter<TripListAdapter.TripViewHolder>() {

  private var data: List<Trip> = emptyList()

  class TripViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(trip: Trip) {
      binding.setVariable(BR.trip, trip)
      binding.setVariable(BR.holder, this)
      val imageView = binding.root.findViewById<ImageView>(R.id.imgTripPreview)
      Glide
        .with(imageView.context)
        .load(FirebaseStorage.getInstance().getReference(trip.picture))
        .into(imageView)
    }

    fun navigateToTripDetails(view: View, trip: Trip) {
      view.findNavController()
        .navigate(
          HomeFragmentDirections.actionNavigationHomeToTripFragment(trip)
        )
    }
  }

  override fun getItemCount(): Int = data.size

  override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
    val binding = DataBindingUtil.inflate<ViewDataBinding>(
      LayoutInflater.from(parent.context),
      R.layout.card_trip,
      parent,
      false
    )
    return TripViewHolder(binding)
  }

  fun setData(newData: List<Trip>) {
    data = newData
    notifyDataSetChanged()
  }
}