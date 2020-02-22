package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.domain.repository.MountainsRepository
import com.hiker.domain.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TripFormViewModel(private val mountainsRepository: MountainsRepository, private val tripsRepository: TripsRepository) : ViewModel(){

    fun getMountains() : LiveData<List<Mountain>> = runBlocking(Dispatchers.IO) {
        mountainsRepository.getAll(false)
    }

    fun addTrip(tripCommand: TripCommand) : LiveData<Int>{
        var tripId = MutableLiveData<Int>()
        viewModelScope.launch {
            tripId.value = tripsRepository.addTrip(tripCommand)
        }
        return tripId
    }

    fun editTrip(tripId: Int, editTripCommand: EditTripCommand){
        viewModelScope.launch {
            tripsRepository.editTrip(tripId, editTripCommand)
        }
    }
}