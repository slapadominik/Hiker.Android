package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.Trip

@Dao
interface TripDao{

    @Query("SELECT * FROM Trip WHERE id == :id")
    fun get(id: Int) : LiveData<Trip?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(trip: Trip)
}