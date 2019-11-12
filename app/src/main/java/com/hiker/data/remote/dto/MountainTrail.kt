package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

class MountainTrail(
    @SerializedName("id") val id: Int,
    @SerializedName("timeToTopMinutes") val timeToTopMinutes: Float,
    @SerializedName("color") val color: String
)