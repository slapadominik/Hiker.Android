package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R

class TripDestinationViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.trip_destination_list_item, parent, false)){

    private var destinationNameTxtView: TextView = itemView.findViewById(R.id.trip_destination_name)
    private var destinationDetailsTxtView: TextView = itemView.findViewById(R.id.trip_destination_details)
    private var destinationIndexTxtView: TextView = itemView.findViewById(R.id.trip_destination_index)

    fun bind(tripDestination: TripDestination){
        destinationIndexTxtView.text = (tripDestination.index+1).toString()+"."
        if (tripDestination.type == 1){
            destinationNameTxtView.text = tripDestination.mountain!!.name
            destinationDetailsTxtView.text = "${tripDestination.mountain.regionName}, ${tripDestination.mountain.metersAboveSeaLevel} m n.p.m."
        }
        else if(tripDestination.type == 2) {
            destinationNameTxtView.text = tripDestination.rock!!.name
            destinationDetailsTxtView.text = "skały"
        }
    }
}