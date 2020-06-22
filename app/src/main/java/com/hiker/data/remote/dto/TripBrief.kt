package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class TripBrief (
    @SerializedName("id") val id: Int,
    @SerializedName("tripTitle") val tripTitle: String,
    @SerializedName("dateFrom") val dateFrom: Date,
    @SerializedName("dateTo") val dateTo: Date,
    @SerializedName("isOneDay") val isOneDay: Boolean,
    @SerializedName("authorId") val authorId: String
)