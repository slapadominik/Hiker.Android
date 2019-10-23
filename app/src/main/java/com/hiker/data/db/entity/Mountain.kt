package com.hiker.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Mountain")
data class Mountain(@PrimaryKey val id: Int,
                    val name: String,
                    val metersAboveSeaLevel: Int)