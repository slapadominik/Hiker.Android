package com.hiker.data.dto

import com.google.gson.annotations.SerializedName


data class FacebookToken(
    @SerializedName("token") val token: String
)