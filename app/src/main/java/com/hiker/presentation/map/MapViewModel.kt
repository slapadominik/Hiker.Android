package com.hiker.presentation.map

import android.widget.ImageView
import androidx.lifecycle.*
import com.hiker.data.converters.asDbModel
import com.hiker.data.db.entity.Mountain
import com.hiker.data.db.repository.MountainLocalRepository
import com.hiker.data.remote.api.ApiConsts
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.data.repository.MountainRemoteRepository
import com.hiker.domain.entities.Resource
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapViewModel(private val mountainsRemoteRepository: MountainRemoteRepository,
                   private val mountainsLocalRepository: MountainLocalRepository) : ViewModel() {

    fun getMountain(mountainId: Int) : Mountain = runBlocking { mountainsLocalRepository.getById(mountainId) }

    fun cacheMountains(mountains: List<MountainBrief>){
        viewModelScope.launch {
            mountainsLocalRepository.addMoutanins(mountains.map { m -> m.asDbModel() })
        }
    }

    fun getMountains() : LiveData<Resource<List<MountainBrief>>> {
        val mountains = MutableLiveData<Resource<List<MountainBrief>>>()
            viewModelScope.launch {
                mountains.postValue(mountainsRemoteRepository.getAll())
            }
        return mountains
    }

    fun getMountaintsWithUpcomingTripsByRadius(latitude: Double, longitude: Double, radius: Int) : LiveData<List<MountainBrief>> {
        val mountains = MutableLiveData<List<MountainBrief>>()
        viewModelScope.launch {
            mountains.postValue(mountainsRemoteRepository.getMountainsWithUpcomingTripsByRadius(latitude, longitude, radius))
        }
        return mountains
    }

    fun getMountainsByName(queryText: String) : LiveData<List<Mountain>>{
        return mountainsLocalRepository.getByName(queryText)
    }

    private fun buildThumbnailUri(mountainId: Int) : String {
        return ApiConsts.HikerAPI+"mountains/$mountainId/thumbnail";
    }
}