package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R
import com.squareup.picasso.Picasso

class UserBriefViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.trip_details_list_item, parent, false)) {

    private var profilePictureImageView: ImageView = itemView.findViewById(R.id.trips_details_profile_image)

    fun bind(userBrief: UserBrief){
        Picasso.get()
            .load(userBrief.profilePictureUrl)
            .into(profilePictureImageView)
    }
}