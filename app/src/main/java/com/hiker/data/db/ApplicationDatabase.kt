package com.hiker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hiker.data.db.converters.DateConverter
import com.hiker.data.db.dao.UserDao
import com.hiker.data.db.entity.User

@Database(entities = arrayOf(User::class), version = 1)
@TypeConverters(DateConverter::class)
public abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "HikeDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}