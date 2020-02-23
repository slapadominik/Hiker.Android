package com.hiker.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDbModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.api.MountainsService
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.MountainsRepository
import java.util.concurrent.TimeUnit

class MountainsRepositoryImpl(private val mountainsDao: MountainsDao) : MountainsRepository{

    private val mountainsService = MountainsService.create()

    override fun getMountainsByName(queryText: String): LiveData<List<Mountain>> {
        return mountainsDao.getMountainsByName(queryText)
    }

    override suspend fun getById(mountainId: Int) : com.hiker.data.remote.dto.Mountain {
        val response = mountainsService.getById(mountainId)
        return if (response.isSuccessful)
            response.body()!!
        else throw ApiException(response.errorBody()?.string())
    }

    override suspend fun getAll(fetchFromRemote: Boolean): LiveData<List<Mountain>> {
        if (fetchFromRemote){
            val response = mountainsService.getAll()
            if (response.isSuccessful){
                val dbModels = response.body()!!.map { m -> m.asDbModel()}
                mountainsDao.addMountains(dbModels)
            }
            else{
                Log.i("MountainsRepositoryImpl", "not respond")
            }
        }
        return mountainsDao.getMountains()
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)

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