package com.hiker.data.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hiker.data.db.entity.Mountain
import com.hiker.data.db.entity.Trip
import com.hiker.data.db.entity.TripMountainCrossRef

data class TripWithMountains(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "mountainId",
        associateBy = Junction(TripMountainCrossRef::class)
    )
    val mountains: List<Mountain>
)