package com.hiker.data.db.repository

import androidx.lifecycle.LiveData
import com.hiker.data.db.dao.MountainsDao
import com.hiker.data.db.entity.Mountain

interface IMountainLocalRepository{
    fun getAll(): LiveData<List<Mountain>>
    fun getByName(queryText: String): LiveData<List<Mountain>>
    suspend  fun getById(mountainId: Int) : Mountain
    suspend fun addMountain(mountain: Mountain)
    suspend fun addMoutanins(mountains: List<Mountain>)
}

class MountainLocalRepository(private val mountainsDao: MountainsDao) : IMountainLocalRepository{
    override fun getAll(): LiveData<List<Mountain>> {
        return mountainsDao.getMountains()
    }

    override suspend fun addMountain(mountain: Mountain) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addMoutanins(mountains: List<Mountain>) {
        mountainsDao.addMountains(mountains)
    }


    override suspend fun getById(mountainId: Int): Mountain {
        return mountainsDao.getMountainById(mountainId)
    }

    override fun getByName(queryText: String): LiveData<List<Mountain>> {
        return mountainsDao.getMountainsByName(queryText)
    }


    companion object {
        private var instance:  MountainLocalRepository? = null

        @Synchronized
        fun getInstance(mountainsDao: MountainsDao):  MountainLocalRepository{
            if (instance == null)
                instance =
                    MountainLocalRepository(mountainsDao)
            return instance as MountainLocalRepository
        }
    }
}