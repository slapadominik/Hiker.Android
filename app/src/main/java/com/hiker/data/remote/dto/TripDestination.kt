package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TripDestination(
    @SerializedName("type") val type: Int,
    @SerializedName("mountainId") val mountainId: Int?,
    @SerializedName("rockId") val rockId: Int?
)