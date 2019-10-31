package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UserBriefAdapter(private val users: List<UserBrief>) : RecyclerView.Adapter<UserBriefViewHolder>() {

    override fun onBindViewHolder(holder: UserBriefViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBriefViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserBriefViewHolder(inflater, parent)
    }

}