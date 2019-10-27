package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.hiker.data.remote.dto.Trip
import com.hiker.data.remote.dto.TripDestination
import com.hiker.domain.entities.Mountain
import kotlinx.android.synthetic.main.fragment_trip_form_view.view.*
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.*
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.view.*
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.view.tripForm_searchView_1

/**
 * A simple [Fragment] subclass.
 */
class TripFormView : Fragment() {

    private val beginTripCalendar = Calendar.getInstance()
    private val endTripCalendar = Calendar.getInstance()
    private val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    private lateinit var tripFormViewModel: TripFormViewModel
    private lateinit var mountains: List<Mountain>
    private val tripDestinations : MutableList<TripDestination> = mutableListOf()

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
            val adapter = MountainArrayAdapter(
                requireContext(), // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                mountains
            )
            destinationRowView.tripForm_searchView_1.setAdapter(adapter)
            destinationRowView.trip_form_removeDestinationBtn.setOnClickListener{
                (destinationRowView.parent as ViewGroup).removeView(destinationRowView)
            }
            destinationRowView.tripForm_searchView_1.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id ->
                val selectedItem =  parent.getItemAtPosition(position) as Mountain
                tripDestinations.add(TripDestination(type = 1, mountainId = selectedItem.id, rockId = null))
                Toast.makeText(requireContext(),"Selected nowe: ${selectedItem.id}",Toast.LENGTH_SHORT).show()
            }
            tripForm_destinations_layout.addView(destinationRowView, tripForm_destinations_layout.childCount)
        }

        tripForm_beginDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                beginTripCalendar.set(year, month, day)
                tripForm_beginDate_editText.setText(dateFormater.format(beginTripCalendar.time))
            }, beginTripCalendar.get(Calendar.YEAR), beginTripCalendar.get(Calendar.MONTH), beginTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tripForm_endDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                endTripCalendar.set(year, month, day)
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
                tripForm_endDate_editText.setText(sdf.format(endTripCalendar.time))
            }, endTripCalendar.get(Calendar.YEAR), endTripCalendar.get(Calendar.MONTH), endTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tripFormViewModel.getMountains().observe(viewLifecycleOwner, Observer { mountains ->
            this.mountains = mountains
            val adapter = MountainArrayAdapter(
                requireContext(), // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                mountains
            )
            tripForm_searchView_1.setAdapter(adapter)
        })


        tripForm_searchView_1.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id ->
            val selectedItem =  parent.getItemAtPosition(position) as Mountain
            tripDestinations.add(TripDestination(type = 1, mountainId = selectedItem.id, rockId = null))
            Toast.makeText(requireContext(),"Selected : ${selectedItem.id}",Toast.LENGTH_SHORT).show()
        }

        trip_form_removeDestinationBtn.setOnClickListener{
            tripForm_destinations_layout.removeView(tripForm_destinationRowLayout)
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)

        upcomingTripsView_submit_button.setOnClickListener{

            val trip = Trip(
                id = null,
                tripTitle = fragment_trip_form_view_tripTitle.text.toString(),
                authorId = userSystemId!!,
                dateFrom = beginTripCalendar.time,
                dateTo = endTripCalendar.time,
                description = fragment_trip_form_view_description.text.toString(),
                tripDestinations = tripDestinations,
                tripParticipants = null
            )
            Log.i("TripFormView", "Id: ${trip.id}, " +
                    "tripTitle: ${trip.tripTitle}, " +
                    "authorId: ${trip.authorId}," +
                    "dateFrom: ${trip.dateFrom}, " +
                    "dateTo: ${trip.dateTo}," +
                    "description: ${trip.description}")
            try{
                tripFormViewModel.addTrip(trip).observe(this, Observer { tripId -> findNavController().popBackStack()})
            }
            catch (ex: Exception){
                Toast.makeText(requireContext(),ex.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initViewModels() {
        tripFormViewModel = ViewModelProviders.of(this, TripFormViewModelFactory(requireContext())).get(TripFormViewModel::class.java)
    }
}
