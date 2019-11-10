package com.hiker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hiker.data.db.converters.DateConverter
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.db.dao.TripParticipantDao
import com.hiker.data.db.dao.UserBriefDao
import com.hiker.data.db.dao.UserDao
import com.hiker.data.db.entity.Mountain
import com.hiker.data.db.entity.TripParticipant
import com.hiker.data.db.entity.User
import com.hiker.data.db.entity.UserBrief

@Database(entities = arrayOf(User::class, Mountain::class, TripParticipant::class, UserBrief::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mountainsDao() : MountainsDao
    abstract fun userBriefDao(): UserBriefDao
    abstract fun tripParticipantDao(): TripParticipantDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}