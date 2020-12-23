package com.hiker.data.remote.api

import com.hiker.data.remote.dto.ChatRoomMessage
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ChatRoomMessagesService{

    @GET("chat/{chatRoomId}/messages")
    suspend fun getChatRoomMessages(@Query("chatRoomId") chatRoomId: Int): Response<List<ChatRoomMessage>>

    companion object {
        fun create(): ChatRoomMessagesService {
            val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(ApiConsts.HikerAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ChatRoomMessagesService::class.java)
        }
    }
}