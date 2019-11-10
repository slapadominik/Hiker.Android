package com.hiker.domain.repository

import androidx.lifecycle.LiveData
import com.hiker.data.db.entity.UserBrief
import com.hiker.domain.entities.User
import java.util.*

interface UserRepository {
    suspend fun getUserByFacebookId(facebookId: String) : User?
    suspend fun registerFacebookUser(facebookToken: String) : UUID
    suspend fun getUserBySystemId(userSystemId: UUID) : LiveData<User?>
    fun getUserBriefs(userIds: List<String>) : LiveData<List<UserBrief>>
    suspend fun addUserToLocalDb(userBrief: UserBrief)
}