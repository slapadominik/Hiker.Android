package com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R
import org.w3c.dom.Text

const val red = "red"
const val blue = "blue"
const val black = "black"
const val green = "green"
const val yellow = "yellow"


class TrailAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val trailColorImage : ImageView = itemView.findViewById(R.id.mountain_trail_item_trailColor)
    private val timeToTopText : TextView = itemView.findViewById(R.id.mountain_trail_item_timeToTop)
    private val timeToTopMinutesText: TextView = itemView.findViewById(R.id.mountain_trail_item_timeToTop_minutes)
    private val textViewMinutes : TextView = itemView.findViewById(R.id.textView_minutes)

    fun bind(trail:Trail){
        when (trail.color){
            blue -> trailColorImage.setImageResource(R.drawable.ic_vc_blue_trail)
            green -> trailColorImage.setImageResource(R.drawable.ic_vc_green_trail)
            red -> trailColorImage.setImageResource(R.drawable.ic_vc_red_trail)
            yellow -> trailColorImage.setImageResource(R.drawable.ic_vc_yellow_trail)
            black -> trailColorImage.setImageResource(R.drawable.ic_vc_black_trail)
        }
        val hours = trail.timeToTopMinutes/60
        val minutes = trail.timeToTopMinutes%60
        timeToTopText.text = hours.toString()
        if (minutes == 0){
            timeToTopMinutesText.visibility = View.GONE
            textViewMinutes.visibility = View.GONE
        }
        else{
            timeToTopMinutesText.text = minutes.toString()
        }

    }
}