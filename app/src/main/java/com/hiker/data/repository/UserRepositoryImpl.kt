package com.hiker.data.repository


import android.content.Context
import androidx.lifecycle.LiveData
import com.hiker.data.converters.asDomainModel
import com.hiker.data.converters.toEditUserCommand
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.db.entity.UserBrief
import com.hiker.domain.entities.User
import com.hiker.data.remote.api.UserService
import com.hiker.data.remote.dto.FacebookToken
import com.hiker.domain.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val TAG : String = "UserRepositoryImpl"

interface UserRepository {
    suspend fun getRemoteUserByFacebookId(facebookId: String) : User?
    suspend fun registerFacebookUser(facebookToken: String) : UUID
    suspend fun getUserBySystemId(userSystemId: UUID): User
    fun getUserBriefs(userIds: List<String>) : LiveData<List<UserBrief>>
    suspend fun addUserToLocalDb(userBrief: UserBrief)
    suspend fun editUser(user: User) : UUID
    suspend fun cacheUser(user: com.hiker.data.db.entity.User)
}

class UserRepositoryImpl(context: Context) : UserRepository {
    private val userService = UserService.create()
    private val database = ApplicationDatabase.getDatabase(context)

    override suspend fun getUserBySystemId(userSystemId: UUID): User {
        val response= userService.getUserBySystemId(userSystemId)
        return if (response.isSuccessful){
            response.body()!!.asDomainModel()
        }
        else throw ApiException(response.message())
    }

    override suspend fun editUser(user: User) : UUID {
        val response= userService.editUser(user.id, user.toEditUserCommand())
        return if (response.isSuccessful){
            response.body()!!
        }
        else throw ApiException(response.message())
    }

    override fun getUserBriefs(userIds: List<String>): LiveData<List<UserBrief>> {
        return database.userBriefDao().getMany(userIds)
    }

    override suspend fun getRemoteUserByFacebookId(facebookId: String): User? {
        val response = userService.getUserByFacebookId(facebookId)
        return if (response.isSuccessful)
            response.body()!!.asDomainModel()
        else null
    }

    override suspend fun addUserToLocalDb(userBrief: UserBrief) {
        withContext(Dispatchers.IO){
            database.userBriefDao().add(userBrief)
        }
    }

    override suspend fun registerFacebookUser(facebookToken: String) : UUID {
        val response = userService.addUser(FacebookToken(facebookToken))
        return if (response.isSuccessful){
            response.body()!!
        }
        else throw ApiException(response.message())
    }

    override suspend fun cacheUser(user: com.hiker.data.db.entity.User){
        database.userDao().insert(user)
    }


    companion object {
        private var instance: UserRepositoryImpl? = null

        @Synchronized
        fun getInstance(context: Context): UserRepositoryImpl{
            if (instance == null)
                instance =
                    UserRepositoryImpl(context)
            return instance as UserRepositoryImpl
        }
    }
}