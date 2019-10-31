package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserBrief(
    @SerializedName("id") val id: UUID,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("profilePictureUrl") val profilePictureUrl: String?
)