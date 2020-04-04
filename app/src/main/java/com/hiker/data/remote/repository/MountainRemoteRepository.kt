package com.hiker.data.remote.repository

import com.hiker.data.remote.api.MountainsService
import com.hiker.data.remote.dto.Mountain
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.domain.exceptions.ApiException
import java.util.concurrent.TimeUnit

interface IMountainRemoteRepository {
    suspend fun getAll() : List<MountainBrief>
    suspend fun getById(mountainId: Int) : Mountain
    suspend fun getMountainsWithUpcomingTripsByRadius(latitude: Double, longitude: Double, radius: Int) : List<MountainBrief>
}

class MountainRemoteRepository() : IMountainRemoteRepository {
    private val mountainsService = MountainsService.create()

    override suspend fun getMountainsWithUpcomingTripsByRadius(latitude: Double, longitude: Double, radius: Int): List<MountainBrief> {
        val response = mountainsService.getMountainsWithUpcomingTripsByRadius(latitude, longitude, radius)
        if (response.isSuccessful){
            return response.body()!!
        }
        else{
            throw ApiException("Request network failed: GET Mountains.")
        }
    }

    override suspend fun getAll(): List<MountainBrief> {
        val response = mountainsService.getAll()
        if (response.isSuccessful){
            return response.body()!!
        }
        else{
            throw ApiException("Request network failed: GET Mountains.")
        }
    }

    override suspend fun getById(mountainId: Int): Mountain {
        val response = mountainsService.getById(mountainId)
        if (response.isSuccessful){
            return response.body()!!
        }
        else{
            throw ApiException("Request network failed: GET Mountains.")
        }
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)

        private var instance: MountainRemoteRepository? = null

        @Synchronized
        fun getInstance(): MountainRemoteRepository {
            if (instance == null)
                instance =
                    MountainRemoteRepository()
            return instance as MountainRemoteRepository
        }
    }
}