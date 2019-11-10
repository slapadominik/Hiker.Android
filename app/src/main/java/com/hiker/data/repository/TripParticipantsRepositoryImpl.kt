package com.hiker.data.repository

import androidx.lifecycle.LiveData
import com.hiker.data.db.dao.TripParticipantDao
import com.hiker.data.db.dao.UserBriefDao
import com.hiker.data.db.entity.TripParticipant
import com.hiker.data.db.entity.UserBrief
import com.hiker.data.remote.api.TripsService
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.TripParticipantsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TripParticipantsRepositoryImpl(private val tripParticipantDao: TripParticipantDao,
                                     private val userBriefDao: UserBriefDao) : TripParticipantsRepository{

    private val tripsService = TripsService.create()

    override suspend fun addTripParticipant(tripId: Int, userId: UUID) {
        val response = tripsService.addTripParticipant(tripId,
            com.hiker.data.remote.dto.TripParticipant(userId.toString(), tripId)
        )
        if (!response.isSuccessful){
            throw ApiException(response.errorBody()?.string())
        }
        withContext(Dispatchers.IO){
            tripParticipantDao.add(TripParticipant(tripId, userId.toString()))
        }
    }

    override fun getTripParticipants(tripId: Int): LiveData<List<TripParticipant>> {
        return tripParticipantDao.getTripParticipants(tripId)
    }

    companion object {
        private var instance: TripParticipantsRepositoryImpl? = null
        @Synchronized
        fun getInstance(tripDao: TripParticipantDao, userBriefDao: UserBriefDao): TripParticipantsRepositoryImpl{
            if (instance == null)
                instance =
                    TripParticipantsRepositoryImpl(tripDao, userBriefDao)
            return instance as TripParticipantsRepositoryImpl
        }
    }
}