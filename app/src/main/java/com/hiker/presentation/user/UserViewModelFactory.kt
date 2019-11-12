package com.hiker.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.repository.UserRepositoryImpl
import com.hiker.domain.repository.UserRepository

class UserViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserRepository::class.java).newInstance(UserRepositoryImpl.getInstance(context))
    }
}