package com.hiker.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.hiker.data.db.entity.UserBrief
import com.hiker.data.remote.api.AuthenticationService
import com.hiker.data.remote.dto.query.LoginQuery
import com.hiker.data.remote.dto.query.LoginQueryResponse
import com.hiker.data.repository.AuthenticationRepository
import com.hiker.data.repository.UserRepository
import com.hiker.domain.entities.Resource
import com.hiker.domain.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.EMPTY_BYTE_ARRAY
import java.util.*

class LoginViewModel(private val userRepository: UserRepository, private val authenticationRepository: AuthenticationRepository) : ViewModel() {
    fun isUserLoggedIn(accessToken: AccessToken?) : LiveData<Boolean>{
        var result = MutableLiveData<Boolean>()
        result.value = accessToken != null
        return result;
    }

    fun login(facebookToken: String) : LiveData<Resource<LoginQueryResponse>>{
        val result = MutableLiveData<Resource<LoginQueryResponse>>()
        viewModelScope.launch {
            result.postValue(authenticationRepository.login(LoginQuery(facebookToken)))
        }
        return result
    }

    fun getUserByFacebookId(facebookId: String) : LiveData<User?> {
        val result = MutableLiveData<User>()
        viewModelScope.launch {
            val user = userRepository.getRemoteUserByFacebookId(facebookId)
            if (user != null){
                result.postValue(user)
            }
            else{
                result.postValue(null)
            }
        }
        return result
    }

    fun registerUserFromFacebook(facebookToken: String) : LiveData<UUID>{
        val result = MutableLiveData<UUID>()
        viewModelScope.launch {
            result.postValue(userRepository.registerFacebookUser(facebookToken))
        }
        return result
    }

    fun getUserBySystemId(userSystemId: UUID) : LiveData<User>{
        val liveData = MutableLiveData<User>()
        viewModelScope.launch {
            userRepository.getUserBySystemId(userSystemId)
        }
        return liveData
    }


    fun addUserToDatabase(userBrief: UserBrief){
        viewModelScope.launch{
            userRepository.addUserToLocalDb(userBrief)
        }
    }
}