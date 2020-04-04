package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hiker.data.db.entity.Trip
import com.hiker.data.db.relations.TripWithMountains

@Dao
interface TripDao{

    @Transaction
    @Query("SELECT * FROM Trip Where tripId == :id")
    fun getTripWithMountains(id: Int): LiveData<TripWithMountains>

    @Query("SELECT * FROM Trip WHERE tripId == :id")
    fun get(id: Int) : LiveData<Trip?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(trip: Trip)
}