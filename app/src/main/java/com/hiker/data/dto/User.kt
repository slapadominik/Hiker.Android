package com.hiker.data.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("id") val id: UUID,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("birthday") val birthday: Date,
    @SerializedName("gender") val gender: Char,
    @SerializedName("facebookId") val facebookId: String
)