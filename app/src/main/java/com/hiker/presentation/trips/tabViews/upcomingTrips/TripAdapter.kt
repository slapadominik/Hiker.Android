package com.hiker.presentation.trips.tabViews.upcomingTrips

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(val trips: List<Trip>, val context: Context): RecyclerView.Adapter<TripViewHolder>(){

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int {
        return trips.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TripViewHolder(inflater, parent)
    }

}