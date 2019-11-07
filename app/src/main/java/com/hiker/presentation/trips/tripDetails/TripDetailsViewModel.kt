package com.hiker.presentation.trips.tripDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.domain.repository.TripsRepository
import kotlinx.coroutines.launch

class TripDetailsViewModel(private val tripsRepository: TripsRepository) : ViewModel(){

    fun getTrip(tripId: Int) : LiveData<TripQuery> {
        val trip = MutableLiveData<TripQuery>()
        viewModelScope.launch {
            trip.value = tripsRepository.getTrip(tripId)
        }
        return trip
    }
}