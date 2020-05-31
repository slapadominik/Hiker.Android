package com.hiker.presentation.trips.tabViews.upcomingTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.repository.TripsRepository
import com.hiker.domain.entities.Resource
import com.hiker.domain.entities.TripBrief
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UpcomingTripsTabViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    private val calendar = Calendar.getInstance()

    fun getUserUpcomingTripsBriefs(userId: String) : LiveData<Resource<List<TripBrief>>>{
        var upcomingTrips = MutableLiveData<Resource<List<TripBrief>>>()
        viewModelScope.launch {
            upcomingTrips.value = tripsRepository.getUserUpcomingTripsBriefs(userId, calendar.time)
        }
        return upcomingTrips
    }
}