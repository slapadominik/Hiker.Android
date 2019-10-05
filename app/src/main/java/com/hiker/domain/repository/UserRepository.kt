package com.hiker.domain.repository

import androidx.lifecycle.LiveData
import com.hiker.data.models.User
import java.util.*

interface UserRepository {
    suspend fun getUserByFacebookId(facebookId: String) : User?
    suspend fun addUser(facebookToken: String) : UUID
    suspend fun getUserBySystemId(userSystemId: UUID) : LiveData<User?>
}