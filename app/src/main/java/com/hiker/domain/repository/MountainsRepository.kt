package com.hiker.domain.repository

import com.hiker.data.remote.dto.Mountain
import com.hiker.domain.entities.Location

interface MountainsRepository {
    fun getByLocation(location: Location)
    suspend fun getAll() : List<Mountain>?
}