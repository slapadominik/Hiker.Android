package com.hiker.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.hiker.data.db.entity.UserBrief
import com.hiker.data.repository.UserRepository
import com.hiker.domain.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun isUserLoggedIn(accessToken: AccessToken?) : LiveData<Boolean>{
        var result = MutableLiveData<Boolean>()
        result.value = accessToken != null
        return result;
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