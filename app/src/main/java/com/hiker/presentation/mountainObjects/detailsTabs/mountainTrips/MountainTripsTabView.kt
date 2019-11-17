package com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiker.R
import com.hiker.presentation.mountainObjects.MountainDetailsViewDirections
import com.hiker.presentation.trips.tabViews.Trip
import com.hiker.presentation.trips.tabViews.TripAdapter
import kotlinx.android.synthetic.main.fragment_mountain_trips_view.*
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_TRIP_DESTINATION_TYPE = "ARG_TRIP_DESTINATION_TYPE"
private const val ARG_MOUNTAIN_ID = "ARG_MOUNTAIN_ID"
private const val ARG_ROCK_ID = "ARG_ROCK_ID"

class MountainTripsTabView : Fragment() {

    private lateinit var mountainTripsTabViewModel: MountainTripsTabViewModel
    private val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

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
                    if (trips.isEmpty()){
                        mountain_details_textView.visibility = View.VISIBLE
                        mountain_details_tripsList.visibility = View.GONE
                    }
                    else{
                        mountain_details_textView.visibility = View.GONE
                        mountain_details_tripsList.visibility = View.VISIBLE
                        mountain_details_tripsList.layoutManager = LinearLayoutManager(activity)
                        mountain_details_tripsList.adapter =
                            TripAdapter(
                                trips.map { tB ->
                                    Trip(
                                        tB.id,
                                        tB.tripTitle,
                                        tB.dateFrom,
                                        tB.dateTo
                                    )
                                },
                                requireContext()
                            )
                            { trip ->
                                val action =
                                    MountainDetailsViewDirections.actionMountainDetailsViewToTripDetailsView()
                                action.tripId = trip.id
                                action.tripTitle = trip.title
                                action.tripDateFrom = dateFormater.format(trip.dateFrom)
                                action.tripDateTo = dateFormater.format(trip.dateTo)
                                findNavController().navigate(action)
                            }
                    }
                })
        }
    }

    private fun initViewModels() {
        mountainTripsTabViewModel = ViewModelProviders.of(this, MountainTripsTabViewModelFactory(requireContext())).get(MountainTripsTabViewModel::class.java)
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