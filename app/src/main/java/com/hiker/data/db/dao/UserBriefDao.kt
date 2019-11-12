package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.UserBrief

@Dao
interface UserBriefDao {

    @Query("SELECT * FROM UserBrief WHERE id IN (:userIds)")
    fun getMany(userIds: List<String>) : LiveData<List<UserBrief>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMany(users: List<UserBrief>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(user: UserBrief)
}