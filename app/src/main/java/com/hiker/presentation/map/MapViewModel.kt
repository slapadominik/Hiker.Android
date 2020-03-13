package com.hiker.presentation.map

import android.widget.ImageView
import androidx.lifecycle.*
import com.hiker.data.converters.asDbModel
import com.hiker.data.db.entity.Mountain
import com.hiker.data.db.repository.MountainLocalRepository
import com.hiker.data.remote.api.ApiConsts
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.data.remote.repository.MountainRemoteRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MapViewModel(private val mountainsRemoteRepository: MountainRemoteRepository,
                   private val mountainsLocalRepository: MountainLocalRepository) : ViewModel() {

    fun getMountain(mountainId: Int) : LiveData<Mountain> {
        return mountainsLocalRepository.getById(mountainId)
    }

    fun cacheMountains(mountains: List<MountainBrief>){
        viewModelScope.launch {
            mountainsLocalRepository.addMoutanins(mountains.map { m -> m.asDbModel() })
        }
    }

    fun getMountains() : LiveData<List<MountainBrief>> {
        val mountains = MutableLiveData<List<MountainBrief>>()
            viewModelScope.launch {
                mountains.postValue(mountainsRemoteRepository.getAll())
            }
        return mountains
    }

    fun setMountainThumbnail(imageView: ImageView, mountainId: Int){
        Picasso.get()
            .load(buildThumbnailUri(mountainId))
            .into(imageView)
    }

    fun getMountainsByName(queryText: String) : LiveData<List<Mountain>>{
        return mountainsLocalRepository.getByName(queryText)
    }

    private fun buildThumbnailUri(mountainId: Int) : String {
        return ApiConsts.HikerAPI+"mountains/$mountainId/thumbnail";
    }
}