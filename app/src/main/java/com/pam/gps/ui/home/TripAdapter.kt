package com.pam.gps.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.pam.gps.R
import com.pam.gps.model.Tour
import kotlinx.android.synthetic.main.card_trip.view.*

class TripAdapter() : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

  private var data: List<Tour> = emptyList()// = liveData.value ?: emptyList()

  class TripViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

  override fun getItemCount(): Int = data.size

  override fun onBindViewHolder(holder: TripAdapter.TripViewHolder, position: Int) {
    holder.view.txtTripTitle.text = data[position].title
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripAdapter.TripViewHolder {
    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.card_trip, parent, false) as CardView

//    liveData.observe(frag.viewLifecycleOwner, Observer { tours -> setData(tours) })

    return TripViewHolder(itemView)
  }

  fun setData(newData: List<Tour>) {
    data = newData
    notifyDataSetChanged()
  }
}