package com.hiker.data.remote.dto.command

import com.google.gson.annotations.SerializedName

data class TripDestinationCommand(
    @SerializedName("type") val type: Int,
    @SerializedName("mountainId") val mountainId: Int?,
    @SerializedName("rockId") val rockId: Int?
)