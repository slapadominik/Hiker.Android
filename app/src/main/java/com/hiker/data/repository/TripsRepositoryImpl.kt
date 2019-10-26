package com.hiker.data.repository

import com.hiker.data.converters.asDomainModel
import com.hiker.data.remote.api.TripsService
import com.hiker.domain.entities.TripBrief
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.TripsRepository
import java.text.SimpleDateFormat
import java.util.*

class TripsRepositoryImpl : TripsRepository {

    private val tripsService = TripsService.create()
    private val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

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

    companion object {
        private var instance: TripsRepositoryImpl? = null

        @Synchronized
        fun getInstance(): TripsRepositoryImpl{
            if (instance == null)
                instance =
                    TripsRepositoryImpl()
            return instance as TripsRepositoryImpl
        }
    }
}