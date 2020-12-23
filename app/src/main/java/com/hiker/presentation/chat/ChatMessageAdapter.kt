package com.hiker.presentation.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChatMessageAdapter(val messages: List<ChatRoomMessage>, val context: Context): RecyclerView.Adapter<ChatMessageViewHolder>(){

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val trip = messages[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatMessageViewHolder(inflater, parent)
    }

}