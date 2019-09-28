package com.hiker.domain.repository

import com.hiker.data.dto.User
import java.util.*

interface UserRepository {
    suspend fun getUserByFacebookId(facebookId: String) : User?
    suspend fun addUser(facebookToken: String) : UUID
}