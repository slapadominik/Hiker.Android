package com.hiker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.remote.api.MountainsSerivce
import com.hiker.domain.entities.Mountain
import com.hiker.domain.repository.MountainsRepository

class MountainsRepositoryImpl(private val mountainsDao: MountainsDao) : MountainsRepository{

    private val mountainsService = MountainsSerivce.create()


    override fun getAllFromDb(): LiveData<List<Mountain>> {
        return Transformations.map(mountainsDao.getMountains()) {
            it.map { m -> m.asDomainModel() }
        }
    }

    override suspend fun getAll(): List<Mountain>? {
       val response = mountainsService.getAll()
        return if (response.isSuccessful)
            response.body()!!.map { m -> m.asDomainModel() }
        else null
    }

    override suspend fun addMountains(mountains: List<Mountain>) {
        mountainsDao.addMountains(mountains.map { m -> m.asDatabaseModel()})
    }

    companion object {
        private var instance: MountainsRepositoryImpl? = null

        @Synchronized
        fun getInstance(mountainsDao: MountainsDao): MountainsRepositoryImpl{
            if (instance == null)
                instance =
                    MountainsRepositoryImpl(mountainsDao)
            return instance as MountainsRepositoryImpl
        }
    }
}