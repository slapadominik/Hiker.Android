package com.hiker.data.repository

import com.hiker.data.remote.api.ChatRoomMessagesService
import com.hiker.data.remote.dto.ChatRoomMessage
import com.hiker.domain.entities.Resource

interface IChatRoomRepository {
    suspend fun getChatRoomMessages(chatRoomId: Int) : Resource<List<ChatRoomMessage>>
}

class ChatRoomRepository() : IChatRoomRepository {
    private val chatRoomService = ChatRoomMessagesService.create()

    override suspend fun getChatRoomMessages(chatRoomId: Int): Resource<List<ChatRoomMessage>> {
        try{
            val response = chatRoomService.getChatRoomMessages(chatRoomId)
            if (response.isSuccessful){
                return Resource.success(response.body()!!)
            }
            else{
                return Resource.error("API timeout", null)
            }
        }
        catch (e: Exception){
            return Resource.error("Brak połączenia z internetem", null)
        }
    }

    companion object {
        private var instance: ChatRoomRepository? = null

        @Synchronized
        fun getInstance(): ChatRoomRepository{
            if (instance == null)
                instance =
                    ChatRoomRepository()
            return instance as ChatRoomRepository
        }
    }
}