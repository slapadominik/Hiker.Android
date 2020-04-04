package com.hiker.data.db.repository

import androidx.lifecycle.LiveData

import com.hiker.data.db.dao.TripMountainCrossRefDao
import com.hiker.data.db.entity.TripMountainCrossRef
import com.hiker.data.db.relations.TripWithMountains

interface TripMountainCrossRefRepository{
    fun getTripWithMountains(tripId: Int): LiveData<TripWithMountains>
    suspend fun add(trip: TripMountainCrossRef)
}

class TripMountainCrossRefRepositoryImpl(private val dao: TripMountainCrossRefDao) : TripMountainCrossRefRepository {
    override suspend fun add(trip: TripMountainCrossRef) {
        dao.add(trip)
    }

    override fun getTripWithMountains(tripId: Int): LiveData<TripWithMountains> {
        return dao.getTripWithMountains(tripId)
    }

    companion object {
        private var instance: TripMountainCrossRefRepository? = null

        @Synchronized
        fun getInstance(dao: TripMountainCrossRefDao):  TripMountainCrossRefRepository{
            if (instance == null)
                instance =
                    TripMountainCrossRefRepositoryImpl(dao)
            return instance as TripMountainCrossRefRepository
        }
    }
}