package com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R

const val red = "red"
const val blue = "blue"
const val black = "black"
const val green = "green"
const val yellow = "yellow"


class TrailAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val trailColorImage : ImageView = itemView.findViewById(R.id.mountain_trail_item_trailColor)
    private val timeToTopText : TextView = itemView.findViewById(R.id.mountain_trail_item_timeToTop)

    fun bind(trail:Trail){
        when (trail.color){
            blue -> trailColorImage.setImageResource(R.drawable.ic_vc_blue_trail)
            green -> trailColorImage.setImageResource(R.drawable.ic_vc_green_trail)
            red -> trailColorImage.setImageResource(R.drawable.ic_vc_red_trail)
            yellow -> trailColorImage.setImageResource(R.drawable.ic_vc_yellow_trail)
            black -> trailColorImage.setImageResource(R.drawable.ic_vc_black_trail)
        }
        timeToTopText.text = trail.timeToTopHours.toString()
    }
}