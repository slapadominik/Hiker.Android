package com.hiker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hiker.data.db.converters.DateConverter
import com.hiker.data.db.dao.*
import com.hiker.data.db.entity.*

@Database(entities = arrayOf(User::class, Mountain::class, TripParticipant::class, UserBrief::class, Trip::class), version = 3)
@TypeConverters(DateConverter::class)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mountainsDao() : MountainsDao
    abstract fun userBriefDao(): UserBriefDao
    abstract fun tripParticipantDao(): TripParticipantDao
    abstract fun tripDao() : TripDao

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
                    "HikeDatabase.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}