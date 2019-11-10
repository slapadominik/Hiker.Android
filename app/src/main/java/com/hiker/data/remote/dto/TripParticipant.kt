package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TripParticipant(
    @SerializedName("userId") val userId: String,
    @SerializedName("tripId") val tripId: Int
)