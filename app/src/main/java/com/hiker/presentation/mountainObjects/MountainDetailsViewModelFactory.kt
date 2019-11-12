package com.hiker.presentation.mountainObjects

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.MountainsRepositoryImpl

class MountainDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return MountainDetailsViewModel(
            MountainsRepositoryImpl.getInstance(db.mountainsDao())
        ) as T
    }

}