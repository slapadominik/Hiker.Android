package com.hiker.presentation.trips.tabViews.historyTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.repository.TripsRepository
import com.hiker.domain.entities.TripBrief
import kotlinx.coroutines.launch
import java.util.*

class HistoryTripsTabViewModel(private val tripsRepository: TripsRepository) : ViewModel(){

    private val calendar = Calendar.getInstance()

    fun getUserHistoryTripsBriefs(userId: String) : LiveData<List<TripBrief>> {
        var upcomingTrips = MutableLiveData<List<TripBrief>>()
        viewModelScope.launch {
            upcomingTrips.value = tripsRepository.getUserHistoryTripsBriefs(userId, calendar.time)
        }
        return upcomingTrips
    }
}