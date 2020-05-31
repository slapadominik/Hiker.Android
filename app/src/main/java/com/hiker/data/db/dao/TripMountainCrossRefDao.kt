package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hiker.data.db.entity.TripMountainCrossRef
import com.hiker.data.db.relations.TripWithMountains

@Dao
interface TripMountainCrossRefDao{

    @Transaction
    @Query("SELECT * FROM Trip Where tripId == :tripId")
    fun getTripWithMountains(tripId: Int): LiveData<TripWithMountains>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(trip: TripMountainCrossRef)

    @Query("DELETE FROM TripMountainCrossRef WHERE tripId == :tripId")
    suspend fun deleteByTripId(tripId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(tripList: List<TripMountainCrossRef>)
}