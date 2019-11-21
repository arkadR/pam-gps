package com.pam.gps.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.model.Trip
import kotlinx.android.synthetic.main.card_trip.view.*
import java.text.SimpleDateFormat

class TripListAdapter : RecyclerView.Adapter<TripListAdapter.TripViewHolder>() {

  private var data: List<Trip> = emptyList()

  class TripViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

  override fun getItemCount(): Int = data.size

  override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
    holder.view.apply {
      setOnClickListener {
        findNavController()
          .navigate(
            HomeFragmentDirections.actionNavigationHomeToTripFragment(data[position])
          )
      }
      txtTripTitle.text = data[position].title
      txtTripDate.text =
              //TODO[ME] Cleaner
        SimpleDateFormat("dd.MM.yyyy").format(data[position].date?.toDate() ?: "")
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