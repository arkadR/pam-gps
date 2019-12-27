package com.pam.gps.ui.home.trip_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.BR
import com.pam.gps.R
import com.pam.gps.model.Trip
import com.pam.gps.ui.home.HomeFragmentDirections

class TripListAdapter : RecyclerView.Adapter<TripListAdapter.TripViewHolder>() {

  private var data: List<Trip> = emptyList()

  class TripViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(trip: Trip) {
      binding.setVariable(BR.trip, trip)
      binding.setVariable(BR.holder, this)
    }

    fun navigateToTripDetails(view: View, trip: Trip) {
      //TODO
      view.findNavController()
        .navigate(
          HomeFragmentDirections.actionNavigationHomeToTripFragmentMain(trip)
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