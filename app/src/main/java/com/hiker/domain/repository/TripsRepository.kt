package com.hiker.domain.repository

import com.hiker.domain.entities.TripBrief
import java.util.*


interface TripsRepository {
    suspend fun getUserUpcomingTripsBriefs(userId: String, dateFrom: Date) : List<TripBrief>
    suspend fun getUserHistoryTripsBriefs(userId: String, dateTo: Date) : List<TripBrief>
}