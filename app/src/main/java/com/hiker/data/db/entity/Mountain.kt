package com.hiker.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "Mountain")
data class Mountain(@PrimaryKey val id: Int,
                    val name: String,
                    val metersAboveSeaLevel: Int,
                    val latitude: Double,
                    val longitude: Double,
                    val upcomingTripsCount : Int,
                    val regionName: String,
                    @ColumnInfo(name = "last_modified", defaultValue = "CURRENT_TIMESTAMP") val lastModified: Long?){
    override fun toString(): String {
        return "$name, $metersAboveSeaLevel m n.p.m."
    }
}