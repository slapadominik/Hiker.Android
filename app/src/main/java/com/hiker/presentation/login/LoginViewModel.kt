package com.hiker.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiker.data.models.User
import com.hiker.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserByFacebookId(facebookId: String) : LiveData<User?> {
        val result = MutableLiveData<User>()
        CoroutineScope(Dispatchers.Default).launch {
            val user = userRepository.getUserByFacebookId(facebookId)
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
        CoroutineScope(Dispatchers.Default).launch {
            result.postValue(userRepository.addUser(facebookToken))
        }
        return result
    }
}