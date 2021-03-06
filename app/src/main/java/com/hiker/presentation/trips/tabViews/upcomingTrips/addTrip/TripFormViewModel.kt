package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.db.entity.Mountain
import com.hiker.data.db.entity.Trip
import com.hiker.data.db.relations.TripWithMountains
import com.hiker.data.db.repository.MountainLocalRepository
import com.hiker.data.db.repository.TripMountainCrossRefRepository
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.data.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TripFormViewModel(private val mountainsLocalRepository: MountainLocalRepository,
                        private val tripsRepository: TripsRepository,
                        private val tripMountainRepository: TripMountainCrossRefRepository) : ViewModel(){

    fun getMountains() = mountainsLocalRepository.getAll()

    fun getTripFromDb(tripId: Int) : LiveData<TripWithMountains> {
        return tripMountainRepository.getTripWithMountains(tripId)
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
