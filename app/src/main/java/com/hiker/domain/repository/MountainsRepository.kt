package com.hiker.domain.repository


import androidx.lifecycle.LiveData
import com.hiker.data.db.entity.Mountain

interface MountainsRepository {
    fun getMountainsByName(queryText: String) : LiveData<List<Mountain>>
    suspend fun getAll(fetchFromRemote: Boolean) : LiveData<List<Mountain>>
    suspend fun getById(mountainId: Int) : com.hiker.data.remote.dto.Mountain
}