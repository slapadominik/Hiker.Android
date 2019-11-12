package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hiker.data.db.entity.TripParticipant


@Dao
interface TripParticipantDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMany(tripParticipants: List<TripParticipant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(tripParticipant: TripParticipant)

    @Delete
    suspend fun delete(tripParticipant: TripParticipant)

    @Query("SELECT * FROM TripParticipant where TripId = :tripId")
    fun getTripParticipants(tripId: Int) : LiveData<List<TripParticipant>>

}