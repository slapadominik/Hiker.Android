package com.hiker.data.remote.dto.command

import com.google.gson.annotations.SerializedName
import java.util.*

data class EditTripCommand(
    @SerializedName("tripTitle") val tripTitle: String,
    @SerializedName("dateFrom") val dateFrom: Date,
    @SerializedName("dateTo") val dateTo: Date,
    @SerializedName("description") val description: String,
    @SerializedName("tripDestinations")  val tripDestinations: List<TripDestinationCommand>
)