package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R
import com.squareup.picasso.Picasso

class UserBriefViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.trip_details_list_item, parent, false)) {

    private var profilePictureImageView: ImageView = itemView.findViewById(R.id.trips_details_profile_image)
    private var firstNameTextView: TextView = itemView.findViewById(R.id.user_item_userFirstName)
    private var lastNameTextView: TextView = itemView.findViewById(R.id.user_item_userLastName)
    private var authorTextView: TextView = itemView.findViewById(R.id.user_item_tripAuthor)
    private var itemLayout: LinearLayout = itemView.findViewById(R.id.user_item_layout)

    fun bind(userBrief: UserBrief, clickListener: (UserBrief) -> Unit){
        Picasso.get()
            .load(userBrief.profilePictureUrl)
            .into(profilePictureImageView)

        firstNameTextView.text = userBrief.firstName
        lastNameTextView.text = userBrief.lastName
        if (userBrief.isAuthor){
            authorTextView.visibility = View.VISIBLE
        }
        else{
            authorTextView.visibility = View.GONE
        }
        itemLayout.setOnClickListener{clickListener(userBrief)}
    }
}