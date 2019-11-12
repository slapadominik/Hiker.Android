package com.hiker.domain.repository


import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.entities.TripBrief
import java.util.*


interface TripsRepository {
    suspend fun getUserUpcomingTripsBriefs(userId: String, dateFrom: Date) : List<TripBrief>
    suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date) : List<TripBrief>
    suspend fun getUpcomingTripsBriefsForMountainObject(tripDestinationType: Int, mountainId: Int?, rockId: Int?, dateFrom: Date) : List<TripBrief>
    suspend fun getTrip(tripId: Int) : TripQuery
    suspend fun addTrip(tripCommand: TripCommand) : Int
}