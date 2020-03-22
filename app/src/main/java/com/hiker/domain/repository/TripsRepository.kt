package com.hiker.domain.repository


import androidx.lifecycle.LiveData
import com.hiker.data.db.entity.Trip
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.entities.TripBrief
import java.util.*


interface TripsRepository {
    suspend fun getUserUpcomingTripsBriefs(userId: String, dateFrom: Date) : List<TripBrief>
    suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date) : List<TripBrief>
    suspend fun getUpcomingTripsBriefsForMountainObject(tripDestinationType: Int, mountainId: Int?, rockId: Int?, dateFrom: Date) : List<TripBrief>
    suspend fun getTrip(tripId: Int) : TripQuery
    fun getTripFromDb(tripId: Int) : LiveData<Trip?>
    suspend fun addTrip(tripCommand: TripCommand) : Int
    suspend fun deleteTrip(tripId: Int)
    suspend fun editTrip(tripId: Int, editTripCommand: EditTripCommand)
}