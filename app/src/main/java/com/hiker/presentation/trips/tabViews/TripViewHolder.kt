package com.hiker.presentation.trips.tabViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R
import com.hiker.presentation.trips.tabViews.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.trip_list_item, parent, false)) {

    private var dateFromTextView: TextView = itemView.findViewById(R.id.trip_list_item_dateFrom)
    private var dateToTextView: TextView = itemView.findViewById(R.id.trip_list_item_dateTo)
    private var titleTextView: TextView = itemView.findViewById(R.id.trip_list_item_title)
    private var dateFormater: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
    private var itemLayout: LinearLayout = itemView.findViewById(R.id.trip_list_item_layout)
    private var minus: TextView = itemView.findViewById(R.id.trip_list_item_minus)
    private var participantTypeTextView: TextView = itemView.findViewById(R.id.trip_list_item_participantType)

    fun bind(trip: Trip, clickListener: (Trip) -> Unit){
        titleTextView.text = trip.title
        dateFromTextView.text = dateFormater.format(trip.dateFrom)

        if (!trip.isOneDay){
            dateToTextView.text = dateFormater.format(trip.dateTo)
        }
        else{
            dateToTextView.text = null
            minus.text = null
        }

        if (trip.isAuthor){
            participantTypeTextView.text = "Organizator"
        }
        else{
            participantTypeTextView.visibility = View.GONE
        }
        
        itemLayout.setOnClickListener{ clickListener(trip) }
    }

}