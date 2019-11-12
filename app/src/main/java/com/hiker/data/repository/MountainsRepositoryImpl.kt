package com.hiker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.remote.api.MountainsService
import com.hiker.domain.entities.Mountain
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.MountainsRepository

class MountainsRepositoryImpl(private val mountainsDao: MountainsDao) : MountainsRepository{

    private val mountainsService = MountainsService.create()


    override suspend fun getById(mountainId: Int) : com.hiker.data.remote.dto.Mountain {
        val response = mountainsService.getById(mountainId)
        return if (response.isSuccessful)
            response.body()!!
        else throw ApiException(response.errorBody()?.string())
    }

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