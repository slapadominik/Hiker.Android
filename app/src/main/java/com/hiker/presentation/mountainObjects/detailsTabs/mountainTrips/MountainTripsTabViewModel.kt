package com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.repository.TripsRepository
import com.hiker.domain.entities.TripBrief
import kotlinx.coroutines.launch
import java.util.*

class MountainTripsTabViewModel(private val tripsRepository: TripsRepository) : ViewModel(){

    private val calendar = Calendar.getInstance()

    fun getUpcomingTripsForMountainObject(tripDestinationType: Int, mountainId: Int?, rockId: Int?) : LiveData<List<TripBrief>>{
        val trips = MutableLiveData<List<TripBrief>>()
        viewModelScope.launch {
            trips.value = tripsRepository.getUpcomingTripsBriefsForMountainObject(tripDestinationType, mountainId, rockId, calendar.time)
        }
        return trips
    }

}