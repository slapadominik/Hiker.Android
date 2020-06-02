package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.Mountain


@Dao
interface MountainsDao {

    @Query("SELECT * FROM Mountain WHERE mountainId == :mountainId")
    suspend fun getMountainById(mountainId: Int) : Mountain

    @Query("SELECT * FROM Mountain WHERE name LIKE :searchText")
    fun getMountainsByName(searchText : String) : LiveData<List<Mountain>>

    @Query("SELECT * FROM Mountain")
    fun getMountains() : LiveData<List<Mountain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMountains(mountains: List<Mountain>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMountain(mountain: Mountain)

    @Query("SELECT EXISTS (SELECT * FROM Mountain WHERE mountainId == :mountainId AND last_modified >= :refreshTime)")
    fun hasMountain(mountainId: Int, refreshTime: Long) : Boolean
}