package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip


import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager

import com.hiker.R
import kotlinx.android.synthetic.main.fragment_trip_form_view.*

/**
 * A simple [Fragment] subclass.
 */
class TripFormView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_form_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tipFormView_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_20dp)
        tipFormView_toolbar.setNavigationOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Uwaga")
                .setMessage("Czy chcesz porzucić wprowadzone zmiany?")
                .setPositiveButton("Tak") { _, _ ->
                    findNavController().popBackStack()
                }
                .setNegativeButton("Nie", /* listener = */ null)
                .show()
        }

        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        upcomingTripsView_addDestination_button.setOnClickListener {
            val destinationRowView = requireActivity().layoutInflater.inflate(R.layout.upcoming_trips_destination_field, null)
            tripForm_destinations_layout.addView(destinationRowView, tripForm_destinations_layout.childCount - 1)
        }
    }
}
