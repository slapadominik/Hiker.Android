package com.hiker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.hiker.R
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDbModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.TripDao
import com.hiker.data.db.dao.TripMountainCrossRefDao
import com.hiker.data.db.dao.TripParticipantDao
import com.hiker.data.db.dao.UserBriefDao
import com.hiker.data.db.entity.Trip
import com.hiker.data.db.entity.TripMountainCrossRef
import com.hiker.data.remote.api.TripsService
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.entities.Resource
import com.hiker.domain.entities.TripBrief
import com.hiker.domain.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

interface TripsRepository {
    suspend fun getUserUpcomingTripsBriefs(userId: String, dateFrom: Date) : Resource<List<TripBrief>>
    suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date) : Resource<List<TripBrief>>
    suspend fun getUpcomingTripsBriefsForMountainObject(tripDestinationType: Int, mountainId: Int?, rockId: Int?, dateFrom: Date) : List<TripBrief>
    suspend fun getTrip(tripId: Int) : TripQuery
    fun getTripFromDb(tripId: Int) : LiveData<Trip?>
    suspend fun addTrip(tripCommand: TripCommand) : Int
    suspend fun deleteTrip(tripId: Int)
    suspend fun editTrip(tripId: Int, editTripCommand: EditTripCommand)
}

class TripsRepositoryImpl(private val tripParticipantDao: TripParticipantDao,
                          private val userBriefDao: UserBriefDao,
                          private val tripDao : TripDao,
                          private val tripMountainCrossRefDao: TripMountainCrossRefDao,
                          val context: Context) : TripsRepository {

    private val tripsService = TripsService.create()
    private val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

    override fun getTripFromDb(tripId: Int): LiveData<Trip?> {
        return tripDao.get(tripId)
    }

    override suspend fun deleteTrip(tripId: Int) {
        val response = tripsService.removeTrip(tripId)
        if (!response.isSuccessful){
            throw ApiException(response.message().toString())
        }
    }

    override suspend fun editTrip(tripId: Int, editTripCommand: EditTripCommand) {
        val response = tripsService.editTrip(tripId, editTripCommand)
        if (response.isSuccessful){
            tripDao.updateTrip(tripId, editTripCommand.tripTitle, editTripCommand.description, editTripCommand.dateFrom, editTripCommand.dateTo, editTripCommand.isOneDay)
            tripMountainCrossRefDao.deleteByTripId(tripId)
            tripMountainCrossRefDao.add(editTripCommand.tripDestinations.map { x -> TripMountainCrossRef(tripId, x.mountainId!!) })
        }
        else{
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
                tripDao.add(trip.asDbModel())
                tripMountainCrossRefDao.add(trip.tripDestinations.map { x -> TripMountainCrossRef(trip.id, x.mountainBrief!!.id) })
            }
            return response.body()!!
        } else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date): Resource<List<TripBrief>> {
        try{
            val response = tripsService.getUserHistoryTripsBriefs(userId, dateFormater.format(dateTo))
            if (response.isSuccessful){
                return Resource.success(response.body()!!.map { x -> x.asDomainModel() })
            }
            return Resource.error(context.getString(R.string.API_error), null)
        }
        catch (exception: Exception){
            return Resource.error(context.getString(R.string.no_internet), null)
        }

    }

    override suspend fun getUserUpcomingTripsBriefs(
        userId: String,
        dateFrom: Date
    ): Resource<List<TripBrief>> {
        try{
            val response = tripsService.getUserIncomingTripsBriefs(userId, dateFormater.format(dateFrom))
            if (response.isSuccessful){
                return Resource.success(response.body()!!.map { x -> x.asDomainModel() })
            }
            return Resource.error(context.getString(R.string.API_error), null)
        }
        catch (exception: Exception){
            return Resource.error(context.getString(R.string.no_internet), null)
        }
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
        fun getInstance(tripParticipantDao: TripParticipantDao,
                        userBriefDao: UserBriefDao,
                        tripDao: TripDao,
                        tripMountainCrossRefDao: TripMountainCrossRefDao,
                        context: Context): TripsRepositoryImpl{
            if (instance == null)
                instance =
                    TripsRepositoryImpl(tripParticipantDao, userBriefDao, tripDao, tripMountainCrossRefDao, context)
            return instance as TripsRepositoryImpl
        }
    }
}