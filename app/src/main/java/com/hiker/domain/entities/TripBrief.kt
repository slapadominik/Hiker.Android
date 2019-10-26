package com.hiker.domain.entities

import java.util.*

data class TripBrief(
    val id: Int,
    val tripTitle: String,
    val dateFrom: Date,
    val dateTo: Date
)