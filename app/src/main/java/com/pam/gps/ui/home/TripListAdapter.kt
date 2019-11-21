package com.pam.gps.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.model.Trip
import kotlinx.android.synthetic.main.card_trip.view.*

class TripListAdapter : RecyclerView.Adapter<TripListAdapter.TripViewHolder>() {

  private var data: List<Trip> = emptyList()

  class TripViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

  override fun getItemCount(): Int = data.size

  override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
    holder.view.apply {
      txtTripTitle.text = data[position].title
//      txtTripDate.text = data[position].date?.toString() ?: ""
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): TripViewHolder {
    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.card_trip, parent, false) as CardView

    return TripViewHolder(itemView)
  }

  fun setData(newData: List<Trip>) {
    data = newData
    notifyDataSetChanged()
  }
}