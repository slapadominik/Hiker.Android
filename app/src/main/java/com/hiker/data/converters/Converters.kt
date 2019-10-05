package com.hiker.data.converters

import com.hiker.data.models.User
import java.util.*


fun com.hiker.data.remote.dto.User.asDomainModel() = User(
    id = id.toString(),
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    facebookId = facebookId
)

fun com.hiker.data.remote.dto.User.asDatabaseModel() = com.hiker.data.db.entity.User(
    id = id.toString(),
    birthday = birthday,
    lastName = lastName,
    firstName = firstName,
    gender = gender,
    facebookId = facebookId
)

fun com.hiker.data.db.entity.User.asDomainModel() = User(
    id = id,
    facebookId = facebookId,
    birthday = birthday,
    firstName = firstName,
    lastName = lastName
)