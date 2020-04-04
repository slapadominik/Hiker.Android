package com.hiker.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["tripId", "mountainId"])
data class TripMountainCrossRef(
    val tripId: Int,
    val mountainId: Int
)