package com.hiker.presentation.trips.tabViews.upcomingTrips

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R
import java.text.SimpleDateFormat
import java.util.*

class TripViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.trip_list_item, parent, false)) {

    private var dateFromTextView: TextView = itemView.findViewById(R.id.trip_list_item_dateFrom)
    private var dateToTextView: TextView = itemView.findViewById(R.id.trip_list_item_dateTo)
    private var titleTextView: TextView = itemView.findViewById(R.id.trip_list_item_title)
    private var participantsTextView: TextView = itemView.findViewById(R.id.trip_list_item_title)
    private var dateFormater: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
    private var itemLayout: LinearLayout = itemView.findViewById(R.id.trip_list_item_layout)

    fun bind(trip: Trip, clickListener: (Trip) -> Unit){
        titleTextView.text = trip.title
        dateFromTextView.text = dateFormater.format(trip.dateFrom)
        dateToTextView.text = dateFormater.format(trip.dateTo)
        itemLayout.setOnClickListener{ clickListener(trip) }
    }

}