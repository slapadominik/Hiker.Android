package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName

class Location(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("regionName") val regionName: String
)