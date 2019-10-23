package com.hiker.domain.entities

import java.util.*

data class User(
     val id: String,
     val firstName: String,
     val lastName: String,
     val birthday: Date,
     val facebookId: String
)