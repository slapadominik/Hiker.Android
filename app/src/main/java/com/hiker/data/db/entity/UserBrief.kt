package com.hiker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hiker.data.db.converters.DateConverter
import java.util.*


@Entity(tableName = "UserBrief")
data class UserBrief(@PrimaryKey val id: String,
                     val firstName: String,
                     val lastName: String,
                     val birthday: Date?,
                     val profilePictureUrl: String
)