package com.hiker.domain.repository

import androidx.lifecycle.LiveData
import com.hiker.data.dto.Mountain
import com.hiker.domain.entities.Location

interface MountainsRepository {
    fun getByLocation(location: Location)
    suspend fun getAll() : List<Mountain>?
}