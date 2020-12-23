package com.hiker.data.converters

import com.hiker.data.db.entity.Trip
import com.hiker.data.db.entity.UserBrief
import com.hiker.data.remote.dto.command.EditUserCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.entities.*


fun com.hiker.data.remote.dto.MountainBrief.asDbModel() = com.hiker.data.db.entity.Mountain(
    mountainId = id,
    name = name,
    metersAboveSeaLevel = metersAboveSeaLevel,
    latitude = location.latitude,
    longitude = location.longitude,
    upcomingTripsCount = upcomingTripsCount,
    regionName = location.regionName,
    lastModified = null)

fun com.hiker.data.remote.dto.Mountain.asDbModel() = com.hiker.data.db.entity.Mountain(
    mountainId = id,
    name = name,
    metersAboveSeaLevel = metersAboveSeaLevel,
    latitude = location.latitude,
    longitude = location.longitude,
    upcomingTripsCount = 0,
    regionName = location.regionName,
    lastModified = null)

fun com.hiker.data.remote.dto.User.asDomainModel() = User(
    id = id.toString(),
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    facebookId = facebookId,
    aboutMe = aboutMe,
    phoneNumber = phoneNumber
)

fun com.hiker.data.remote.dto.User.asDatabaseModel() = com.hiker.data.db.entity.User(
    userId = id.toString(),
    birthday = birthday,
    lastName = lastName,
    firstName = firstName,
    facebookId = facebookId,
    aboutMe = aboutMe,
    phoneNumber = phoneNumber
)
fun User.toEditUserCommand() = EditUserCommand(
    firstName = firstName,
    lastName = lastName,
    phoneNumber =phoneNumber,
    birthday = birthday,
    aboutMe = aboutMe
)

fun com.hiker.data.db.entity.User.asDomainModel() = User(
    id = userId,
    facebookId = facebookId,
    birthday = birthday,
    firstName = firstName,
    lastName = lastName,
    aboutMe = aboutMe,
    phoneNumber = phoneNumber
)


fun User.asDbModel() =com.hiker.data.db.entity.User(
    userId = id,
    facebookId = facebookId,
    birthday = birthday,
    firstName = firstName,
    lastName = lastName,
    aboutMe = aboutMe,
    phoneNumber = phoneNumber
)

fun com.hiker.data.remote.dto.TripBrief.asDomainModel() = TripBrief(
    id = id,
    tripTitle = tripTitle,
    dateFrom = dateFrom,
    dateTo = dateTo,
    isOneDay = isOneDay,
    authorId = authorId
)


fun com.hiker.data.remote.dto.Rock.asDomainModel() = Rock(
    id = id,
    name = name,
    location = Location(location.latitude, location.longitude, location.regionName)
)

fun com.hiker.data.remote.dto.query.UserBrief.asDatabaseModel() = UserBrief(
    userId = id.toString(),
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    profilePictureUrl = profilePictureUrl
)

fun com.hiker.data.remote.dto.User.asUserBrief() = UserBrief(
    userId = id.toString(),
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    profilePictureUrl = "https://graph.facebook.com/${facebookId}/picture?width=300&height=300"
)

fun User.asUserBrief() = UserBrief(
    userId = id,
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    profilePictureUrl = "https://graph.facebook.com/${facebookId}/picture?width=300&height=300"
)

fun TripQuery.asDbModel() = Trip(
    tripId = id,
    title = tripTitle,
    dateFrom = dateFrom,
    dateTo = dateTo,
    description = description,
    isOneDay = isOneDay,
    authorId = author.id.toString()
)