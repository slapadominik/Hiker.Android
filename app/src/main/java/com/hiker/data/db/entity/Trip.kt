package com.hiker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Trip")
data class Trip(@PrimaryKey val tripId: Int,
                val title: String,
                val description: String,
                val dateFrom: Date,
                val dateTo: Date?,
                val isOneDay: Boolean,
                val authorId: String)