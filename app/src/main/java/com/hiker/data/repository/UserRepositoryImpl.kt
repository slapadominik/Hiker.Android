package com.hiker.data.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.db.entity.UserBrief
import com.hiker.domain.entities.User
import com.hiker.data.remote.api.UserService
import com.hiker.data.remote.dto.FacebookToken
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val TAG : String = "UserRepositoryImpl"

class UserRepositoryImpl(context: Context) : UserRepository {

    private val userService = UserService.create()
    private val database = ApplicationDatabase.getDatabase(context)

    override suspend fun getUserBySystemId(userSystemId: UUID): LiveData<User?> {
        refreshUser(userSystemId)
        return Transformations.map(database.userDao().getById(userSystemId.toString())){
            it?.asDomainModel()
        }
    }

    override fun getUserBriefs(userIds: List<String>): LiveData<List<UserBrief>> {
        return database.userBriefDao().getMany(userIds)
    }

    override suspend fun getUserByFacebookId(facebookId: String): User? {
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

    private suspend fun refreshUser(userSystemId: UUID){
            withContext(Dispatchers.IO){
                val user = userService.getUserBySystemId(userSystemId)
                if (user.isSuccessful){
                    if (user.body() != null){
                        database.userDao().insert(user.body()!!.asDatabaseModel())
                        Log.d(TAG, "User found in remote service.")
                    }
                    else{
                        Log.d(TAG, "User not found in remote serivce.")
                    }
                }
            }
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