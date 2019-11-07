package com.hiker.data.remote.dto.query

import com.google.gson.annotations.SerializedName
import java.util.*

data class TripQuery(
    @SerializedName("id") val id: Int?,
    @SerializedName("tripTitle") val tripTitle: String,
    @SerializedName("author") val author: UserBrief,
    @SerializedName("dateFrom") val dateFrom: Date,
    @SerializedName("dateTo") val dateTo: Date,
    @SerializedName("description") val description: String,
    @SerializedName("tripDestinations")  val tripDestinations: List<TripDestinationQuery>?,
    @SerializedName("tripParticipants")  val tripParticipants: List<UserBrief>?
)
