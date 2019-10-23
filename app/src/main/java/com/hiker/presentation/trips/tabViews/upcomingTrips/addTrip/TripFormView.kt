package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_trip_form_view.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer

/**
 * A simple [Fragment] subclass.
 */
class TripFormView : Fragment() {

    private val beginTripCalendar = Calendar.getInstance()
    private val endTripCalendar = Calendar.getInstance()
    private lateinit var tripFormViewModel: TripFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_form_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        tipFormView_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_20dp)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
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

        upcomingTripsView_addDestination_button.setOnClickListener {
            val destinationRowView = requireActivity().layoutInflater.inflate(R.layout.upcoming_trips_destination_field, null)
            tripForm_destinations_layout.addView(destinationRowView, tripForm_destinations_layout.childCount - 1)
        }

        tripForm_beginDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                beginTripCalendar.set(year, month, day)
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
                tripForm_beginDate_editText.setText(sdf.format(beginTripCalendar.time))
            }, beginTripCalendar.get(Calendar.YEAR), beginTripCalendar.get(Calendar.MONTH), beginTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tripForm_endDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                endTripCalendar.set(year, month, day)
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
                tripForm_endDate_editText.setText(sdf.format(endTripCalendar.time))
            }, endTripCalendar.get(Calendar.YEAR), endTripCalendar.get(Calendar.MONTH), endTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tripFormViewModel.getMountains().observe(viewLifecycleOwner, Observer { result ->
            val mountainsNames = result.map { it.name }.toTypedArray()
            val adapter = ArrayAdapter<String>(
                requireContext(), // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                mountainsNames
            )
            tripForm_searchView_1.setAdapter(adapter)
        })


        tripForm_searchView_1.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(),"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViewModels() {
        tripFormViewModel = ViewModelProviders.of(this, TripFormViewModelFactory(requireContext())).get(TripFormViewModel::class.java)
    }
}
