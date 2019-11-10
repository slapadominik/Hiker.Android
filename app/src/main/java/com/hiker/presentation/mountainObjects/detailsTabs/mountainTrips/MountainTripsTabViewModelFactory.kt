package com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.TripsRepositoryImpl
import com.hiker.domain.repository.TripsRepository

class MountainTripsTabViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return modelClass.getConstructor(TripsRepository::class.java).newInstance(
            TripsRepositoryImpl.getInstance(db.tripParticipantDao(), db.userBriefDao())
        )
    }

}