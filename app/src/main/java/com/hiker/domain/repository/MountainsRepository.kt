package com.hiker.domain.repository


import androidx.lifecycle.LiveData
import com.hiker.domain.entities.Mountain

interface MountainsRepository {
    fun getAllFromDb() : LiveData<List<Mountain>>
    suspend fun getAll() : List<Mountain>?
    suspend fun addMountains(mountains: List<Mountain>)
}