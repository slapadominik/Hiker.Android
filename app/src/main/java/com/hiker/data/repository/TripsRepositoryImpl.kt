package com.hiker.data.repository

import androidx.lifecycle.LiveData
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.TripParticipantDao
import com.hiker.data.db.dao.UserBriefDao
import com.hiker.data.remote.api.TripsService
import com.hiker.data.remote.dto.TripParticipant
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.entities.TripBrief
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TripsRepositoryImpl(private val tripParticipantDao: TripParticipantDao,
                          private val userBriefDao: UserBriefDao) : TripsRepository {

    private val tripsService = TripsService.create()
    private val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

    override suspend fun deleteTrip(tripId: Int) {
        val response = tripsService.removeTrip(tripId)
        if (!response.isSuccessful){
            throw ApiException(response.message().toString())
        }
    }

    override suspend fun getTrip(tripId: Int): TripQuery {
        val response = tripsService.getTripDetails(tripId)
        if (response.isSuccessful){
            val trip = response.body()!!
            withContext(Dispatchers.IO) {
                userBriefDao.addMany(trip.tripParticipants.map { userBrief -> userBrief.asDatabaseModel()} + trip.author.asDatabaseModel())
                tripParticipantDao.addMany(trip.tripParticipants.map { userBrief -> com.hiker.data.db.entity.TripParticipant(trip.id, userBrief.id.toString())}
                        + com.hiker.data.db.entity.TripParticipant(trip.id, trip.author.id.toString()))
            }
            return response.body()!!
        } else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date): List<TripBrief> {
        val response = tripsService.getUserHistoryTripsBriefs(userId, dateFormater.format(dateTo))
        return if (response.isSuccessful){
            response.body()!!.map { x -> x.asDomainModel() }
        } else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun getUserUpcomingTripsBriefs(
        userId: String,
        dateFrom: Date
    ): List<TripBrief> {
        val response = tripsService.getUserIncomingTripsBriefs(userId, dateFormater.format(dateFrom))
        return if (response.isSuccessful){
            response.body()!!.map { x -> x.asDomainModel() }
        } else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun addTrip(tripCommand: TripCommand) : Int {
        val response = tripsService.addTrip(tripCommand)
        return if (response.isSuccessful){
            response.body()!!
        } else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun getUpcomingTripsBriefsForMountainObject(tripDestinationType: Int, mountainId: Int?, rockId: Int?, dateFrom: Date): List<TripBrief> {
        val response = tripsService.getTripBriefs(tripDestinationType, mountainId, rockId, dateFormater.format(dateFrom))
        return if (response.isSuccessful){
            response.body()!!.map { x -> x.asDomainModel() }
        } else throw ApiException(response.errorBody()?.string())
    }


    companion object {
        private var instance: TripsRepositoryImpl? = null
        @Synchronized
        fun getInstance(tripParticipantDao: TripParticipantDao, userBriefDao: UserBriefDao): TripsRepositoryImpl{
            if (instance == null)
                instance =
                    TripsRepositoryImpl(tripParticipantDao, userBriefDao)
            return instance as TripsRepositoryImpl
        }
    }
}