package com.hiker.presentation.trips.tabViews.historyTrips

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.db.ApplicationDatabase
import com.hiker.data.repository.TripsRepositoryImpl

class HistoryripsTabViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = ApplicationDatabase.getDatabase(context)
        return HistoryTripsTabViewModel(
            TripsRepositoryImpl.getInstance(db.tripParticipantDao(), db.userBriefDao(), db.tripDao(), db.tripMountainCrossRefDao(), context)
        ) as T
    }

}