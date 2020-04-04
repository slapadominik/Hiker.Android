package com.hiker.presentation.trips.tripDetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.TripParticipantsRepositoryImpl
import com.hiker.data.repository.TripsRepositoryImpl
import com.hiker.data.repository.UserRepositoryImpl
import com.hiker.domain.repository.TripsRepository

class TripDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return TripDetailsViewModel(
            TripParticipantsRepositoryImpl.getInstance(db.tripParticipantDao(), db.userBriefDao()),
            TripsRepositoryImpl.getInstance(db.tripParticipantDao(), db.userBriefDao(), db.tripDao(), db.tripMountainCrossRefDao()),
            UserRepositoryImpl(context)
        ) as T
    }

}