package com.hiker.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiker.R

class ChatMessageViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.chat_message_item, parent, false)) {

    private var messageTextView: TextView = itemView.findViewById(R.id.chat_item_txt)

    fun bind(message: ChatRoomMessage){
        messageTextView.text = message.content
    }

}