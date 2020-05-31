package com.hiker.data.remote.repository

import android.util.Log
import com.hiker.data.remote.api.MountainsService
import com.hiker.data.remote.dto.Mountain
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.domain.entities.Resource
import com.hiker.domain.exceptions.ApiException
import java.util.concurrent.TimeUnit

interface IMountainRemoteRepository {
    suspend fun getAll() : Resource<List<MountainBrief>>
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

    override suspend fun getAll(): Resource<List<MountainBrief>> {
        try{
            val response = mountainsService.getAll()
            if (response.isSuccessful){
                return Resource.success(response.body()!!)
            }
            else{
                return Resource.error("API timeout", null)
            }
        }
        catch (e: Exception){
            return Resource.error("Brak połączenia z internetem", null)
        }
    }

    override suspend fun getById(mountainId: Int): Mountain {
        val response = mountainsService.getById(mountainId)
        if (response.isSuccessful){
            return response.body()!!
        }
        else{
            throw ApiException(response.message())
        }
    }

    companion object {
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