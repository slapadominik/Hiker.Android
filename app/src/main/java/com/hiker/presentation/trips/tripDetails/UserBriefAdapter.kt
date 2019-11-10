package com.hiker.presentation.trips.tripDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UserBriefAdapter : RecyclerView.Adapter<UserBriefViewHolder>() {
    private var users: List<UserBrief> = mutableListOf()

    fun setData(items: List<UserBrief>){
        this.users = items
        notifyDataSetChanged()
        notifyItemInserted(this.users.size)
    }
    override fun onBindViewHolder(holder: UserBriefViewHolder, position: Int) = holder.bind(users[position])
    override fun getItemCount() = users.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBriefViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserBriefViewHolder(inflater, parent)
    }
}