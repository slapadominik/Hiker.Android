package com.hiker.presentation.map

import android.widget.ImageView
import androidx.lifecycle.*
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.api.ApiConsts
import com.hiker.domain.repository.MountainsRepository
import com.squareup.picasso.Picasso

class MapViewModel(private val mountainsRepository : MountainsRepository) : ViewModel() {

    fun getMountain(mountainId: Int) : LiveData<Mountain> {
        return mountainsRepository.getByIdLocal(mountainId)
    }

    fun getMountains() : LiveData<List<Mountain>> {
        return mountainsRepository.getAll(true)
    }

    fun setMountainThumbnail(imageView: ImageView, mountainId: Int){
        Picasso.get()
            .load(buildThumbnailUri(mountainId))
            .into(imageView)
    }

    fun getMountainsByName(queryText: String) : LiveData<List<Mountain>>{
        return mountainsRepository.getMountainsByName(queryText)
    }

    private fun buildThumbnailUri(mountainId: Int) : String {
        return ApiConsts.HikerAPI+"mountains/$mountainId/thumbnail";
    }
}