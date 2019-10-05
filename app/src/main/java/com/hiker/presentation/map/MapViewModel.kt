package com.hiker.presentation.map

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiker.data.remote.api.ApiConsts
import com.hiker.data.remote.dto.Mountain
import com.hiker.domain.repository.MountainsRepository
import com.squareup.picasso.Picasso
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

    fun setMountainThumbnail(imageView: ImageView, mountainId: Int){
        Picasso.get()
            .load(buildThumbnailUri(mountainId))
            .into(imageView)
    }

    private fun buildThumbnailUri(mountainId: Int) : String {
        return ApiConsts.HikerAPI+"mountains/$mountainId/thumbnail";
    }
}