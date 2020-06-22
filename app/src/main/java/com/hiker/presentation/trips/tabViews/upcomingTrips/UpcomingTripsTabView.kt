package com.hiker.presentation.trips.tabViews.upcomingTrips


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hiker.R
import com.hiker.domain.consts.OperationType
import com.hiker.domain.entities.Status
import com.hiker.domain.extensions.empty
import com.hiker.presentation.trips.TripsViewDirections
import com.hiker.presentation.trips.tabViews.Trip
import com.hiker.presentation.trips.tabViews.TripAdapter
import com.hiker.presentation.trips.tripDetails.TripDetailsViewDirections
import kotlinx.android.synthetic.main.fragment_upcoming_trips_tab_view.*
import java.text.SimpleDateFormat


class UpcomingTripsTabView : Fragment() {

    private lateinit var upcomingTripsTabViewModel: UpcomingTripsTabViewModel
    private val dateFormater  = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming_trips_tab_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupOnClicks()
        setupTripsList()
    }

    private fun setupOnClicks(){
        upcomingTripsView_addTrip_button.setOnClickListener {
            val action = TripsViewDirections.actionTripsViewToTripFormView(OperationType.Add, null, null, null, null, -1)
            findNavController().navigate(action)
        }
    }

    private fun setupTripsList(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)

        upcomingTripsTabViewModel.getUserUpcomingTripsBriefs(userSystemId!!).observe(this, Observer { response ->
            if (response.status == Status.SUCCESS){
                val tripsBriefs = response.data!!
                if (tripsBriefs.isEmpty()){
                    upcomingTrips_emptyList_textView.visibility = View.VISIBLE
                    upcomingTripsView_recyclerView.visibility = View.GONE
                }
                else{
                    upcomingTrips_emptyList_textView.visibility = View.GONE
                    upcomingTripsView_recyclerView.visibility = View.VISIBLE
                    upcomingTripsView_recyclerView.layoutManager = LinearLayoutManager(activity)
                    val orderedTrips = tripsBriefs.sortedBy { x -> x.dateFrom }
                    upcomingTripsView_recyclerView.adapter =
                        TripAdapter(orderedTrips.map { x ->
                            Trip(
                                x.id,
                                x.tripTitle,
                                x.dateFrom,
                                x.dateTo,
                                x.isOneDay,
                                x.authorId == userSystemId
                            )
                        }, requireContext()) {
                            val action = TripsViewDirections.actionTripsViewToTripDetailsView()
                            action.tripId = it.id
                            action.tripTitle = it.title
                            action.tripDateFrom = dateFormater.format(it.dateFrom)
                            action.tripDateTo = if (it.dateTo != null) dateFormater.format(it.dateTo) else String.empty()
                            action.isOneDay = it.isOneDay
                            findNavController().navigate(action)
                        }
                }
            }
            else{
                val snack = Snackbar.make(requireView(), response.message!!, Snackbar.LENGTH_LONG)
                snack.anchorView = upcomingTripsView_addTrip_button
                snack.show()
            }
        })
    }

    private fun initViewModel(){
        upcomingTripsTabViewModel = ViewModelProviders.of(this, UpcomingTripsTabViewModelFactory(requireContext())).get(UpcomingTripsTabViewModel::class.java)
    }
}
