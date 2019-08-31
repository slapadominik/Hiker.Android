package com.hiker.data.dto

import com.google.gson.annotations.SerializedName

class Mountain(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("metersAboveSeaLevel") val metersAboveSeaLevel: Int,
    @SerializedName("location") val location : Location,
    @SerializedName("trails") val trails: MountainTrailBrief
)

