package com.hiker.data.remote.dto.query

import com.google.gson.annotations.SerializedName
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.data.remote.dto.Rock

data class TripDestinationQuery(
    @SerializedName("type") val type: Int,
    @SerializedName("mountainBrief") val mountainBrief: MountainBrief?,
    @SerializedName("rock") val rock: Rock?
)