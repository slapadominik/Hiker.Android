package com.hiker.presentation.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.ChatRoomRepository


class ChatRoomViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewViewModel(
            ChatRoomRepository.getInstance()
        ) as T
    }
}