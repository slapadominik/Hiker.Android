package com.hiker.domain.repository


import androidx.lifecycle.LiveData
import com.hiker.data.db.entity.Mountain

interface MountainsRepository {
    fun getMountainsByName(queryText: String) : LiveData<List<Mountain>>
    fun getAll(fetchFromRemote: Boolean) : LiveData<List<Mountain>>
    fun getByIdLocal(mountainId: Int) : LiveData<Mountain>
    suspend fun getByIdRemote(mountainId: Int) : com.hiker.data.remote.dto.Mountain
}