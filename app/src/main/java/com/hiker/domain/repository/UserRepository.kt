package com.hiker.domain.repository

import com.hiker.data.dto.User

interface UserRepository {
    suspend fun getUserByFacebookId(facebookId: String) : User?
}