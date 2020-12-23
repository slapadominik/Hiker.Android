package com.hiker.data.remote.dto.query

import com.google.gson.annotations.SerializedName

data class LoginQuery(
    @SerializedName("FacebookToken") val token: String
)