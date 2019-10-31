package com.hiker.presentation.trips.tripDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.repository.TripsRepositoryImpl
import com.hiker.domain.repository.TripsRepository

class TripDetailsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TripsRepository::class.java).newInstance(TripsRepositoryImpl.getInstance())
    }

}