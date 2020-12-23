package com.hiker.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.repository.AuthenticationRepository
import com.hiker.data.repository.UserRepository
import com.hiker.data.repository.UserRepositoryImpl

class LoginViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(UserRepositoryImpl.getInstance(context), AuthenticationRepository.getInstance()) as T
    }

}