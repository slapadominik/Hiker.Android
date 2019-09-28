package com.hiker.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiker.data.dto.User
import com.hiker.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserByFacebookId(facebookId: String) : LiveData<User?> {
        val result = MutableLiveData<User>()
        CoroutineScope(Dispatchers.Default).launch {
            val user = userRepository.getUserByFacebookId(facebookId)
            if (user != null){
                result.postValue(user)
            }
        }
        return result
    }
}