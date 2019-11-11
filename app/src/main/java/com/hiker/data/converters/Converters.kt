package com.hiker.data.converters

import com.hiker.data.db.entity.UserBrief
import com.hiker.data.remote.dto.MountainTrailBrief
import com.hiker.domain.entities.*


fun com.hiker.data.remote.dto.Mountain.asDomainModel() = Mountain(
    id = id,
    name = name,
    metersAboveSeaLevel = metersAboveSeaLevel,
    location = Location(location.latitude, location.longitude, location.regionName),
    trails = MountainTrailBrief(trails.href, trails.count)
)

fun com.hiker.data.db.entity.Mountain.asDomainModel() = Mountain(
    id = id,
    name = name,
    metersAboveSeaLevel = metersAboveSeaLevel,
    location = null,
    trails = null
)

fun Mountain.asDatabaseModel() = com.hiker.data.db.entity.Mountain(
    id = id,
    name = name,
    metersAboveSeaLevel = metersAboveSeaLevel
)

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

fun com.hiker.data.remote.dto.TripBrief.asDomainModel() = TripBrief(
    id = id,
    tripTitle = tripTitle,
    dateFrom = dateFrom,
    dateTo = dateTo
)


fun com.hiker.data.remote.dto.Rock.asDomainModel() = Rock(
    id = id,
    name = name,
    location = Location(location.latitude, location.longitude, location.regionName)
)

fun com.hiker.data.remote.dto.query.UserBrief.asDatabaseModel() = UserBrief(
    id = id.toString(),
    firstName = firstName,
    lastName = lastName,
    profilePictureUrl = profilePictureUrl
)

fun User.asUserBrief() = UserBrief(
    id = id,
    firstName = firstName,
    lastName = lastName,
    profilePictureUrl = "https://graph.facebook.com/${facebookId}/picture?width=300&height=300"
)

