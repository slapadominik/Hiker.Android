package com.hiker.data.repository

import com.hiker.data.api.MountainsSerivce
import com.hiker.data.dto.Mountain
import com.hiker.domain.entities.Location
import com.hiker.domain.repository.MountainsRepository

class MountainsRepositoryImpl : MountainsRepository{
    private val mountainsService = MountainsSerivce.create()

    override fun getByLocation(location: Location) {

    }

    override suspend fun getAll(): List<Mountain>? {
       val response = mountainsService.getAll()
        return if (response.isSuccessful)
            response.body()
        else null
    }
}