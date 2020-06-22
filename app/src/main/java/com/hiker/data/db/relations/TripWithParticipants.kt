package com.hiker.data.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hiker.data.db.entity.Trip
import com.hiker.data.db.entity.TripParticipant
import com.hiker.data.db.entity.UserBrief

data class TripWithParticipants(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "userId",
        associateBy = Junction(TripParticipant::class)
    )
    val participants:  List<UserBrief>
)