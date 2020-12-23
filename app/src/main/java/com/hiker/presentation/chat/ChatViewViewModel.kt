package com.hiker.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.remote.dto.ChatRoomMessage
import com.hiker.data.repository.ChatRoomRepository
import com.hiker.domain.entities.Resource
import kotlinx.coroutines.launch

class ChatViewViewModel(private val chatRoomRepository: ChatRoomRepository) : ViewModel() {

    fun getChatRoomMessages(chatRoomId: Int) : LiveData<Resource<List<ChatRoomMessage>>>{
        val messages = MutableLiveData<Resource<List<ChatRoomMessage>>>()
        viewModelScope.launch {
            messages.postValue(chatRoomRepository.getChatRoomMessages(chatRoomId))
        }
        return messages
    }
}
