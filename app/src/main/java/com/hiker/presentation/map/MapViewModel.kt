package com.hiker.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiker.data.dto.Mountain
import com.hiker.domain.repository.MountainsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val mountainsRepository : MountainsRepository) : ViewModel() {

    fun getAllMountains() : LiveData<List<Mountain>> {
        val result = MutableLiveData<List<Mountain>>()

        CoroutineScope(Dispatchers.Default).launch {
            val mountains = mountainsRepository.getAll()
            if (mountains != null){
                result.postValue(mountains)
            }
        }
        return result
    }
}