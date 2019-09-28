package com.hiker.data.repository

import com.hiker.data.api.UserService
import com.hiker.data.dto.FacebookToken
import com.hiker.data.dto.User
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.UserRepository
import java.util.*

class UserRepositoryImpl : UserRepository {
    private val userService = UserService.create()

    override suspend fun getUserByFacebookId(facebookId: String): User? {
        val response = userService.getUser(facebookId)
        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun addUser(facebookToken: String) : UUID {
        val response = userService.addUser(FacebookToken(facebookToken))
        return if (response.isSuccessful){
            response.body()!!
        }
        else throw ApiException(response.message())
    }
}