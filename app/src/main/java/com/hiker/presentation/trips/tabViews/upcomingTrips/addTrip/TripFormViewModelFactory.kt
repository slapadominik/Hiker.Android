package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.repository.MountainsRepositoryImpl
import com.hiker.domain.repository.MountainsRepository

class TripFormViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MountainsRepository::class.java).newInstance(MountainsRepositoryImpl.getInstance(context))
    }

}