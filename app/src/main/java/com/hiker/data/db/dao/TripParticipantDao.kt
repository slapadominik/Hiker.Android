package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.TripParticipant


@Dao
interface TripParticipantDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMany(tripParticipants: List<TripParticipant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(tripParticipant: TripParticipant)

    @Query("SELECT * FROM TripParticipant where TripId = :tripId")
    fun getTripParticipants(tripId: Int) : LiveData<List<TripParticipant>>

}