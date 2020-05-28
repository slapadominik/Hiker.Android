package com.hiker.data.db.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDate(value: Long?): Date?{
        if (value != null) {
            return Date(value)
        }
        return null
    }

    @TypeConverter
    fun toLong(date: Date?): Long? = date?.time
}