package com.hiker.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class Trip(
    @SerializedName("id") val id: Int?,
    @SerializedName("tripTitle") val tripTitle: String,
    @SerializedName("authorId") val authorId: String,
    @SerializedName("dateFrom") val dateFrom: Date,
    @SerializedName("dateTo") val dateTo: Date,
    @SerializedName("description") val description: String,
    @SerializedName("tripDestinations")  val tripDestinations: List<TripDestination>?,
    @SerializedName("tripParticipants")  val tripParticipants: List<User>?)