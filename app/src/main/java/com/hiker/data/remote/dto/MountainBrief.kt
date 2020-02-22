package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

class MountainBrief(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("metersAboveSeaLevel") val metersAboveSeaLevel: Int,
    @SerializedName("location") val location : Location,
    @SerializedName("upcomingTripsCount") val upcomingTripsCount : Int
)

