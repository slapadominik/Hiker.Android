package com.hiker.presentation.trips.tripDetails

data class UserBrief(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val isAuthor: Boolean
)