package com.hiker.presentation.mountainObjects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.remote.dto.Mountain
import com.hiker.domain.repository.MountainsRepository
import kotlinx.coroutines.launch

class MountainDetailsViewModel(private val mountainsRepository: MountainsRepository) : ViewModel(){

    fun getMountainDetails(mountainId: Int) : LiveData<com.hiker.data.remote.dto.Mountain> {
        val mountain = MutableLiveData<com.hiker.data.remote.dto.Mountain>()
        viewModelScope.launch {
            mountain.value = mountainsRepository.getByIdRemote(mountainId)
        }
        return mountain
    }
}