package com.hiker.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.domain.repository.MountainsRepository

class MapViewModelFactory(private val mountainsRepository : MountainsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(mountainsRepository) as T
    }
}