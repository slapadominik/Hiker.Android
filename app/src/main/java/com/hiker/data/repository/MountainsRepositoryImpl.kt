package com.hiker.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDbModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.api.MountainsService
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.repository.MountainsRepository
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class MountainsRepositoryImpl(private val mountainsDao: MountainsDao) : MountainsRepository{

    private val mountainsService = MountainsService.create()

    override fun getMountainsByName(queryText: String): LiveData<List<Mountain>> {
        return mountainsDao.getMountainsByName(queryText)
    }

    override suspend fun getByIdRemote(mountainId: Int): com.hiker.data.remote.dto.Mountain {
        val response = mountainsService.getById(mountainId)
        if (response.isSuccessful){
            return response.body()!!
        }
        throw ApiException(response.errorBody()?.string())
    }

    override fun getByIdLocal(mountainId: Int) : LiveData<Mountain> {
        return mountainsDao.getMountainById(mountainId)
    }

    override fun getAll(fetchFromRemote: Boolean): LiveData<List<Mountain>> {
        if (fetchFromRemote){
            liveData(Dispatchers.IO){
                emitSource(mountainsDao.getMountains())
                val response = mountainsService.getAll()
                if (response.isSuccessful){
                    val dbModels = response.body()!!.map { m -> m.asDbModel()}
                    mountainsDao.addMountains(dbModels)
                }
                else{
                    Log.i("MountainsRepositoryImpl", "not respond")
                }
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