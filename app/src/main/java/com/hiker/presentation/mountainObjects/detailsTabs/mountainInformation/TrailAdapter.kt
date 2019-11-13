package com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R

class TrailAdapter(val context: Context, private val trails: List<Trail>) : RecyclerView.Adapter<TrailAdapterViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailAdapterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mountain_trail_list_item, null)
        return TrailAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trails.count()
    }

    override fun onBindViewHolder(holder: TrailAdapterViewHolder, position: Int) {
        val trail = trails[position]
        holder.bind(trail)
    }

}