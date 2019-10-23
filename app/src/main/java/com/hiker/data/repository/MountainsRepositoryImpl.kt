package com.hiker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hiker.data.converters.asDatabaseModel
import com.hiker.data.converters.asDomainModel
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.remote.api.MountainsSerivce
import com.hiker.domain.entities.Mountain
import com.hiker.domain.repository.MountainsRepository

class MountainsRepositoryImpl(context: Context) : MountainsRepository{

    private val mountainsService = MountainsSerivce.create()
    private val database = ApplicationDatabase.getDatabase(context)


    override fun getAllFromDb(): LiveData<List<Mountain>> {
        return Transformations.map(database.mountainsDao().getMountains()) {
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
        database.mountainsDao().addMountains(mountains.map { m -> m.asDatabaseModel()})
    }

    companion object {
        private var instance: MountainsRepositoryImpl? = null

        @Synchronized
        fun getInstance(context: Context): MountainsRepositoryImpl{
            if (instance == null)
                instance =
                    MountainsRepositoryImpl(context)
            return instance as MountainsRepositoryImpl
        }
    }
}