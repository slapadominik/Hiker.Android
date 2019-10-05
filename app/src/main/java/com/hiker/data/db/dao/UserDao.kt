package com.hiker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User where Id = :id")
    fun getById(id: String) : LiveData<User?>

    @Query("SELECT * FROM User where Id = :id LIMIT 1")
    suspend fun hasUser(id: String) : User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}