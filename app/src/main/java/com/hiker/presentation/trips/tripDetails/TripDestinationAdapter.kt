package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TripDestinationAdapter(private val tripDestinations: List<TripDestination>) : RecyclerView.Adapter<TripDestinationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDestinationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TripDestinationViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
       return tripDestinations.count()
    }

    override fun onBindViewHolder(holder: TripDestinationViewHolder, position: Int) {
       val tripDestination = tripDestinations[position]
        holder.bind(tripDestination)
    }
}