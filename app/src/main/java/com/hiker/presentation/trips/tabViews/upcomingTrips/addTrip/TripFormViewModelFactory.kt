package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.db.repository.MountainLocalRepository
import com.hiker.data.db.repository.TripMountainCrossRefRepository
import com.hiker.data.db.repository.TripMountainCrossRefRepositoryImpl
import com.hiker.data.repository.TripsRepository
import com.hiker.data.repository.TripsRepositoryImpl

class TripFormViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return modelClass.getConstructor(MountainLocalRepository::class.java, TripsRepository::class.java, TripMountainCrossRefRepository::class.java)
            .newInstance(
                MountainLocalRepository.getInstance(db.mountainsDao()),
                TripsRepositoryImpl.getInstance(db.tripParticipantDao(), db.userBriefDao(), db.tripDao(), db.tripMountainCrossRefDao(), context),
                TripMountainCrossRefRepositoryImpl.getInstance(db.tripMountainCrossRefDao())
                )

    }
}