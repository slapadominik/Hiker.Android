package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatRoomMessage(
    @SerializedName("content") val content: String,
    @SerializedName("username") val username: String,
    @SerializedName("userImgUrl") val userImgUrl: String
)