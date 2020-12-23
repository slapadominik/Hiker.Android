package com.hiker.presentation.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.db.repository.MountainLocalRepository
import com.hiker.data.repository.MountainRemoteRepository

class MapViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return MapViewModel(
            MountainRemoteRepository.getInstance(),
            MountainLocalRepository(db.mountainsDao())
        ) as T
    }
}