package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiker.domain.entities.Mountain
import com.hiker.domain.repository.MountainsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TripFormViewModel(private val mountainsRepository: MountainsRepository) : ViewModel(){

    fun getMountains() : LiveData<List<Mountain>> = runBlocking(Dispatchers.IO) {
        mountainsRepository.getAllFromDb()
    }
}