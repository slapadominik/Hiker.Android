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
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_upcoming_trips_tab_view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class UpcomingTripsTabView : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var upcomingTripsTabViewModel: UpcomingTripsTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
            findNavController().navigate(R.id.tripFormView)
        }
    }

    private fun setupTripsList(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)

        upcomingTripsTabViewModel.getUserUpcomingTripsBriefs(userSystemId!!).observe(this, Observer { tripsBriefs ->
            upcomingTripsView_recyclerView.layoutManager = LinearLayoutManager(activity)
            upcomingTripsView_recyclerView.adapter = TripAdapter(tripsBriefs.map { x -> Trip(x.tripTitle, x.dateFrom, x.dateTo) }, requireContext())
        })
    }

    private fun initViewModel(){
        upcomingTripsTabViewModel = ViewModelProviders.of(this, UpcomingTripsTabViewModelFactory()).get(UpcomingTripsTabViewModel::class.java)
    }
}
