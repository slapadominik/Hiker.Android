package com.hiker.presentation.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.MountainsRepositoryImpl
import com.hiker.data.repository.UserRepositoryImpl
import com.hiker.domain.repository.MountainsRepository
import com.hiker.domain.repository.UserRepository

class MapViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return modelClass.getConstructor(MountainsRepository::class.java).newInstance(
            MountainsRepositoryImpl.getInstance(db.mountainsDao())
        )
    }
}