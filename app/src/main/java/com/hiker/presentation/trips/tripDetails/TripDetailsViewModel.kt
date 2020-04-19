package com.hiker.presentation.trips.tripDetails

import androidx.lifecycle.*
import com.hiker.data.remote.dto.TripParticipant
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.query.TripQuery
import com.hiker.data.repository.TripParticipantsRepository
import com.hiker.data.repository.TripsRepository
import com.hiker.data.repository.UserRepository
import com.hiker.domain.extensions.Period
import kotlinx.coroutines.launch
import java.util.*

class TripDetailsViewModel(private val tripParticipantsRepository: TripParticipantsRepository,
                           private val tripsRepository: TripsRepository,
                           private val userRepository: UserRepository
) : ViewModel(){

    fun getTrip(tripId: Int) : LiveData<TripQuery> {
        val trip = MutableLiveData<TripQuery>()
        viewModelScope.launch {
            trip.value = tripsRepository.getTrip(tripId)
        }
        return trip
    }

    fun getUsersBriefs(userIds: List<String>) : LiveData<List<UserBrief>> {
        return Transformations.map(userRepository.getUserBriefs(userIds)){
            it.map { x -> UserBrief(x.id, x.firstName, x.lastName, Period.between(x.birthday, Calendar.getInstance().time).toString(), x.profilePictureUrl) }
        }
    }

    fun getTripParticipants(tripId: Int) : LiveData<List<com.hiker.data.db.entity.TripParticipant>> {
        return tripParticipantsRepository.getTripParticipants(tripId)
    }

    fun addTripParticipant(tripId: Int, userId: UUID){
        viewModelScope.launch {
            tripParticipantsRepository.addTripParticipant(tripId, userId)
        }
    }

    fun removeTripParticipant(tripId: Int, userId: UUID){
        viewModelScope.launch {
            tripParticipantsRepository.removeTripParticipant(tripId, userId)
        }
    }

    fun removeTrip(tripId: Int){
        viewModelScope.launch {
            tripsRepository.deleteTrip(tripId)
        }
    }
}