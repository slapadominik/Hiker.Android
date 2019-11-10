package com.hiker.data.db.entity

import androidx.room.Entity

@Entity(tableName = "TripParticipant", primaryKeys = ["tripId", "userId"])
data class TripParticipant(
    val tripId: Int,
    val userId: String)