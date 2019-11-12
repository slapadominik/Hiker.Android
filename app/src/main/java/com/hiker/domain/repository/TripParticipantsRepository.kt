package com.hiker.domain.repository

import androidx.lifecycle.LiveData
import com.hiker.data.db.entity.TripParticipant
import com.hiker.data.db.entity.UserBrief
import java.util.*

interface TripParticipantsRepository{
    suspend fun addTripParticipant(tripId: Int, userId: UUID)
    suspend fun removeTripParticipant(tripId: Int, userId: UUID)
    fun getTripParticipants(tripId: Int): LiveData<List<TripParticipant>>
}