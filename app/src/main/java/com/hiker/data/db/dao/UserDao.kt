package com.hiker.data.db.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hiker.data.db.entity.User
import retrofit2.Response
import retrofit2.http.GET

@Dao
interface UserDao {

    @Query("SELECT * FROM User where Id = :id")
    fun getById(id: String) : LiveData<User?>

    @Query("SELECT * FROM User where Id = :id LIMIT 1")
    suspend fun hasUser(id: String) : User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
}