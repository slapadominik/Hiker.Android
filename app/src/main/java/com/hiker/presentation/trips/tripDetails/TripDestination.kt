package com.hiker.presentation.trips.tripDetails

import com.hiker.data.db.entity.Mountain
import com.hiker.domain.entities.Rock

data class TripDestination(
    val index: Int,
    val type: Int,
    val mountain: Mountain?,
    val rock: Rock?
)