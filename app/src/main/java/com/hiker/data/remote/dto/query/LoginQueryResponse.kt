package com.hiker.data.remote.dto.query

import com.google.gson.annotations.SerializedName
import com.hiker.data.remote.dto.User

data class LoginQueryResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)