package com.hiker.presentation.trips.tabViews.upcomingTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.domain.entities.TripBrief
import com.hiker.domain.repository.TripsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UpcomingTripsTabViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    private val calendar = Calendar.getInstance()

    fun getUserUpcomingTripsBriefs(userId: String) : LiveData<List<TripBrief>>{
        var upcomingTrips = MutableLiveData<List<TripBrief>>()
        viewModelScope.launch {
            upcomingTrips.value = tripsRepository.getUserUpcomingTripsBriefs(userId, calendar.time)
        }
        return upcomingTrips
    }
}