package com.hiker.data.repository

import com.hiker.data.api.UserService
import com.hiker.data.dto.User
import com.hiker.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val userService = UserService.create()

    override suspend fun getUserByFacebookId(facebookId: String): User? {
        val response = userService.getUser(facebookId)
        return if (response.isSuccessful)
            response.body()
        else null
    }

}