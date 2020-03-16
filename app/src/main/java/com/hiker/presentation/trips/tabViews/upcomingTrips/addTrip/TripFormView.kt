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
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_trip_form_view.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.command.TripDestinationCommand
import com.hiker.domain.consts.OperationType
import com.hiker.domain.extensions.setUpMarker
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.*
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.view.*
import kotlinx.android.synthetic.main.upcoming_trips_destination_field.view.tripForm_searchView_1


class TripFormView : Fragment(), OnMapReadyCallback {


    private val beginTripCalendar = Calendar.getInstance()
    private val endTripCalendar = Calendar.getInstance()
    private val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    private lateinit var tripFormViewModel: TripFormViewModel
    private lateinit var mountains: List<Mountain>
    private val tripDestinations : MutableMap<Int, TripDestinationCommand> = mutableMapOf()
    private lateinit var googleMapView: com.google.android.gms.maps.MapView
    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val markersMap : MutableMap<Int, Marker> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_form_view, container, false)
        googleMapView = view.findViewById(R.id.mapView3)
        googleMapView.onCreate(savedInstanceState)
        googleMapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return view;
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 7.5f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        arguments?.let {
            val safeArgs = TripFormViewArgs.fromBundle(it)
            if (safeArgs.operationType == OperationType.Add){
                setupAddOnclickListeners()
                requireActivity().onBackPressedDispatcher.addCallback(this) {
                    showAlertDialog()
                }
            }
            if (safeArgs.operationType == OperationType.Edit){
                tipFormView_toolbar.title = "Edytuj wycieczkę"
                setupEditOnclickListeners(safeArgs.tripId)
                fillTextFields(safeArgs.tripTitle, safeArgs.tripDescription)
            }
            setupOnClickListeners()
        }
    }

    private fun fillTextFields(tripTitle: String?, tripDescription: String?){
        fragment_trip_form_view_tripTitle.setText(tripTitle)
        fragment_trip_form_view_description.setText(tripDescription)
    }

    private fun showAlertDialog() : androidx.appcompat.app.AlertDialog{
        return androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Uwaga")
            .setMessage("Czy chcesz porzucić wprowadzone zmiany?")
            .setPositiveButton("Tak") { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Nie", /* listener = */ null)
            .show()
    }

    private fun setupAddOnclickListeners(){
        tipFormView_toolbar.setNavigationOnClickListener {
            showAlertDialog()
        }
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)
        upcomingTripsView_submit_button.setOnClickListener{
            if (fragment_trip_form_view_tripTitle.text.toString().isEmpty()) {
                fragment_trip_form_view_tripTitle.error = "Nazwa jest wymagana"
            }
            else if (tripDestinations.isEmpty()){

            }
            else{
                val trip = TripCommand(
                    tripTitle = fragment_trip_form_view_tripTitle.text.toString(),
                    authorId = userSystemId!!,
                    dateFrom = beginTripCalendar.time,
                    dateTo = endTripCalendar.time,
                    description = fragment_trip_form_view_description.text.toString(),
                    tripDestinations = tripDestinations.values.toList()
                )
                Log.i("TripFormView",
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
    }

    private fun setupEditOnclickListeners(tripId: Int){
        upcomingTripsView_submit_button.setOnClickListener {
            Log.d("TripFormView", "OperationType = EDIT")
        }
        upcomingTripsView_submit_button.setOnClickListener{
            try{
                tripFormViewModel.editTrip(
                    tripId,
                    EditTripCommand(fragment_trip_form_view_tripTitle.text.toString(),
                        beginTripCalendar.time,
                        endTripCalendar.time,
                        description = fragment_trip_form_view_description.text.toString(),
                        tripDestinations = tripDestinations.values.toList()))
            }
            catch (ex: Exception){
                Toast.makeText(requireContext(),ex.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupOnClickListeners(){
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
            val index = 1
            tripDestinations[index] = TripDestinationCommand(
                type = 1,
                mountainId = selectedItem.id,
                rockId = null
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedItem.latitude, selectedItem.longitude),7.5f))
            val marker = googleMap.setUpMarker(selectedItem.latitude, selectedItem.longitude, selectedItem.name, requireContext())
            markersMap[index] = marker
        }

        trip_form_removeDestinationBtn.setOnClickListener{
            tripForm_destinations_layout.removeView(tripForm_destinationRowLayout)
            val index = 1
            tripDestinations.remove(index)
            val marker = markersMap[index]
            marker?.remove()
            markersMap.remove(index)
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
                val viewRowIndex = (destinationRowView.parent as ViewGroup).indexOfChild(destinationRowView)
                tripDestinations.remove(viewRowIndex)
                (destinationRowView.parent as ViewGroup).removeView(destinationRowView)
                val marker = markersMap[viewRowIndex]
                marker?.remove()
                markersMap.remove(viewRowIndex)
            }
            destinationRowView.tripForm_searchView_1.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id ->
                val selectedItem =  parent.getItemAtPosition(position) as Mountain
                val viewRowIndex = (destinationRowView.parent as ViewGroup).indexOfChild(destinationRowView)
                tripDestinations[viewRowIndex] = TripDestinationCommand(
                    type = 1,
                    mountainId = selectedItem.id,
                    rockId = null
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedItem.latitude, selectedItem.longitude),7.5f))
                val marker = googleMap.setUpMarker(selectedItem.latitude, selectedItem.longitude, selectedItem.name, requireContext())
                markersMap[viewRowIndex] = marker
            }
            tripForm_destinations_layout.addView(destinationRowView, tripForm_destinations_layout.childCount)
        }
    }

    private fun initViewModels() {
        tripFormViewModel = ViewModelProviders.of(this, TripFormViewModelFactory(requireContext())).get(TripFormViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()
        googleMapView.onPause()
    }
    override fun onResume() {
        super.onResume()
        googleMapView.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
        googleMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        googleMapView.onLowMemory()
    }
}
