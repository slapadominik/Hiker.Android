package com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiker.R
import com.hiker.presentation.trips.tabViews.upcomingTrips.Trip
import com.hiker.presentation.trips.tabViews.upcomingTrips.TripAdapter
import kotlinx.android.synthetic.main.fragment_mountain_trips_view.*


private const val ARG_TRIP_DESTINATION_TYPE = "ARG_TRIP_DESTINATION_TYPE"
private const val ARG_MOUNTAIN_ID = "ARG_MOUNTAIN_ID"
private const val ARG_ROCK_ID = "ARG_ROCK_ID"

class MountainTripsTabView : Fragment() {

    private lateinit var mountainTripsTabViewModel: MountainTripsTabViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mountain_trips_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        arguments?.let {
            val tripDestinationType = it.getInt(ARG_TRIP_DESTINATION_TYPE)
            var mountainId : Int? = it.getInt(ARG_MOUNTAIN_ID)
            var rockId : Int? = it.getInt(ARG_ROCK_ID)

            if (tripDestinationType == 1){
                rockId = null
            }
            else if (tripDestinationType == 2){
                mountainId = null
            }
            mountainTripsTabViewModel.getUpcomingTripsForMountainObject(tripDestinationType, mountainId, rockId)
                .observe(this, Observer { trips ->
                    mountain_details_tripsList.layoutManager = LinearLayoutManager(activity)
                    mountain_details_tripsList.adapter = TripAdapter(
                        trips.map { tB -> Trip(tB.id, tB.tripTitle, tB.dateFrom, tB.dateTo) },
                        requireContext()
                    ) { t -> Toast.makeText(requireContext(), "Id: "+t.id, Toast.LENGTH_LONG).show()}
                })
        }



    }

    private fun initViewModels() {
        mountainTripsTabViewModel = ViewModelProviders.of(this, MountainTripsTabViewModelFactory()).get(MountainTripsTabViewModel::class.java)
    }

    companion object {
        fun create(tripDestinationType: Int, mountainId: Int, rockId: Int) =
            MountainTripsTabView().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TRIP_DESTINATION_TYPE, tripDestinationType)
                    putInt(ARG_MOUNTAIN_ID, mountainId)
                    putInt(ARG_ROCK_ID, rockId)
                }
            }
    }
}