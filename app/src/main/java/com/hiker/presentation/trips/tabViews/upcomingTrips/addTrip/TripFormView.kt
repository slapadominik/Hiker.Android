package com.hiker.presentation.trips.tabViews.upcomingTrips.addTrip

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
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
import com.google.android.material.chip.Chip
import com.hiker.data.db.entity.Mountain
import com.hiker.data.remote.dto.command.EditTripCommand
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.command.TripDestinationCommand
import com.hiker.domain.consts.OperationType
import com.hiker.domain.extensions.isNullOrEmpty
import com.hiker.domain.extensions.setUpMarker
import kotlinx.android.synthetic.main.upcoming_trips_destination_main_field.*


class TripFormView : Fragment(), OnMapReadyCallback {


    private val beginTripCalendar = Calendar.getInstance()
    private val endTripCalendar = Calendar.getInstance()
    private val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    private lateinit var viewModel: TripFormViewModel
    private lateinit var mountains: List<Mountain>

    private var destinationsCount = 0;
    private var dateFrom: Date? = null
    private var dateTo: Date? = null
    private var isOneDay = true
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showAlertDialog()
        }


        arguments?.let {
            val safeArgs = TripFormViewArgs.fromBundle(it)
            if (safeArgs.operationType == OperationType.Add){
                setupAddTripOnClickListeners()
            }
            if (safeArgs.operationType == OperationType.Edit){
                tipFormView_toolbar.title = "Edytuj wycieczkę"
                viewModel.getTripFromDb(safeArgs.tripId).observe(this, Observer {
                    if (it != null){
                        setUpEditFields(it.trip.title, it.trip.description, it.trip.dateFrom, it.trip.dateTo, it.mountains, it.trip.isOneDay)
                    }
                    else{
                        Toast.makeText(requireContext(), "Wystąpił błąd", Toast.LENGTH_LONG).show()
                    }
                })
                setupEditOnclickListeners(safeArgs.tripId)
            }
            setupOnClickListeners()
        }
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

    private fun setUpEditFields(tripTitle: String?,
                                tripDescription: String?,
                                dateFrom: Date,
                                dateTo: Date?,
                                mountains: List<Mountain>,
                                isOneDay: Boolean){
        this.dateFrom = dateFrom
        this.dateTo = dateTo
        this.isOneDay = isOneDay

        fragment_trip_form_view_tripTitle.setText(tripTitle)
        fragment_trip_form_view_description.setText(tripDescription)
        tripForm_beginDate_editText.setText(dateFormater.format(dateFrom))

        if (!isOneDay){
            tripForm_endDate_editInput.isEnabled = true
            tripForm_endDate_editText.setText(dateFormater.format(dateTo))
            radioBtn_ManyDays.isChecked = true
        }

        mountains.forEach{
            val chip = createChip(it)
            trip_form_chipGroup.addView(chip)

            tripDestinations[destinationsCount] = TripDestinationCommand(
                type = 1,
                mountainId = it.mountainId,
                rockId = null
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude),7.5f))
            val marker = googleMap.setUpMarker(it.latitude, it.longitude, it.name, requireContext())
            markersMap[destinationsCount] = marker
            destinationsCount++
        }
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

    private fun ValidateTripTitle() : Boolean{
        if (isNullOrEmpty(fragment_trip_form_view_tripTitle.text.toString())) {
            fragment_trip_form_view_tripInput.error = "Nazwa jest wymagana"
            return false
        }
        else{
            fragment_trip_form_view_tripInput.error = null;
            return true
        }
    }

    private fun ValidateDestinations() : Boolean {
        if (tripDestinations.isEmpty()){
            tripForm_searchView.error = "Wymagany jest co najmniej 1 cel"
            return false
        }
        tripForm_searchView.error = null
        return true
    }

    private fun ValidateDateFrom() : Boolean {
        if (dateFrom == null){
            tripForm_beginDate_editInput.error = "Data jest wymagana";
            return false
        }
        else if (dateFrom!! < Calendar.getInstance().time){
            tripForm_beginDate_editInput.error = "Wycieczka nie może zaczynać się w przeszłości"
            return false
        }
        else{
            tripForm_beginDate_editInput.error = null
            return true
        }
    }

    private fun ValidateDateTo() : Boolean {
        if (isOneDay){
            return true
        }
        else{
            if (dateTo == null){
                tripForm_endDate_editInput.error = "Data jest wymagana"
                return false
            }
            else if (dateTo!! < Calendar.getInstance().time){
                tripForm_endDate_editInput.error = "Wycieczka nie może kończyć się w przeszłości"
                return false
            }
            else{
                tripForm_endDate_editInput.error = null
                return true
            }
        }
    }

    private fun ValidateDateToDateFrom() : Boolean {
        if (dateFrom != null && dateTo != null && dateFrom!!>dateTo!!){
            tripForm_beginDate_editInput.error = "Data początkowa musi być wcześniejsza od daty końcowej";
            return false
        }
        return true
    }

    private fun setupAddTripOnClickListeners(){
        tipFormView_toolbar.setNavigationOnClickListener {
            showAlertDialog()
        }
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)
        upcomingTripsView_submit_button.setOnClickListener{
            val destinationsValid = ValidateDestinations()
            val tripTitleValid = ValidateTripTitle()
            val dateFromValid = ValidateDateFrom()
            val dateToValid = ValidateDateTo()
            val dateFromDateToValid = ValidateDateToDateFrom()

            if (destinationsValid && tripTitleValid && dateFromValid && dateToValid && dateFromDateToValid){
                val trip = TripCommand(
                    tripTitle = fragment_trip_form_view_tripTitle.text.toString(),
                    authorId = userSystemId!!,
                    dateFrom = dateFrom!!,
                    dateTo = dateTo,
                    isOneDay = isOneDay,
                    description = fragment_trip_form_view_description.text.toString(),
                    tripDestinations = tripDestinations.values.toList()
                )
                try{
                    viewModel.addTrip(trip).observe(this, Observer { tripId-> findNavController().popBackStack()})
                }
                catch (ex: Exception){
                    Toast.makeText(requireContext(),ex.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupEditOnclickListeners(tripId: Int){
        upcomingTripsView_submit_button.setOnClickListener{
            val destinationsValid = ValidateDestinations()
            val tripTitleValid = ValidateTripTitle()
            val dateFromValid = ValidateDateFrom()
            val dateToValid = ValidateDateTo()
            val dateFromDateToValid = ValidateDateToDateFrom()

            if (destinationsValid && tripTitleValid && dateFromValid && dateToValid && dateFromDateToValid){
                try{
                    viewModel.editTrip(
                        tripId,
                        EditTripCommand(fragment_trip_form_view_tripTitle.text.toString(),
                            dateFrom!!,
                            dateTo,
                            isOneDay,
                            description = fragment_trip_form_view_description.text.toString(),
                            tripDestinations = tripDestinations.values.toList()))
                    findNavController().popBackStack()
                }
                catch (ex: Exception){
                    Toast.makeText(requireContext(),ex.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupOnClickListeners(){
        radioBtn_ManyDays.setOnCheckedChangeListener{radioBtn, isChecked ->
            tripForm_endDate_editInput.isEnabled = isChecked
            if (!isChecked){
                isOneDay = true
                dateTo = null
                tripForm_endDate_editText.text?.clear()
            }
            else{
                isOneDay = false
            }
        }
        tripForm_beginDate_editText.setOnFocusChangeListener{x, hasFocus ->
            if (hasFocus){
                DatePickerDialog(requireContext(), {view, year, month, day ->
                    beginTripCalendar.set(year, month, day)
                    tripForm_beginDate_editText.setText(dateFormater.format(beginTripCalendar.time))
                    dateFrom = beginTripCalendar.time
                }, beginTripCalendar.get(Calendar.YEAR), beginTripCalendar.get(Calendar.MONTH), beginTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        tripForm_beginDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                beginTripCalendar.set(year, month, day)
                tripForm_beginDate_editText.setText(dateFormater.format(beginTripCalendar.time))
                dateFrom = beginTripCalendar.time
            }, beginTripCalendar.get(Calendar.YEAR), beginTripCalendar.get(Calendar.MONTH), beginTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        tripForm_endDate_editText.setOnFocusChangeListener{x, hasFocus ->
            if (hasFocus){
                DatePickerDialog(requireContext(), {view, year, month, day ->
                    endTripCalendar.set(year, month, day)
                    tripForm_endDate_editText.setText(dateFormater.format(endTripCalendar.time))
                    dateTo = endTripCalendar.time
                }, endTripCalendar.get(Calendar.YEAR), endTripCalendar.get(Calendar.MONTH), endTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        tripForm_endDate_editText.setOnClickListener{
            DatePickerDialog(requireContext(), {view, year, month, day ->
                endTripCalendar.set(year, month, day)
                tripForm_endDate_editText.setText(dateFormater.format(endTripCalendar.time))
                dateTo = endTripCalendar.time
            }, endTripCalendar.get(Calendar.YEAR), endTripCalendar.get(Calendar.MONTH), endTripCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewModel.getMountains().observe(viewLifecycleOwner, Observer { mountains ->
            this.mountains = mountains
            val adapter = MountainArrayAdapter(
                requireContext(), // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                mountains
            )
            tripForm_searchView.setAdapter(adapter)
        })
        tripForm_searchView.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id ->
            val selectedItem =  parent.getItemAtPosition(position) as Mountain
            val chip = createChip(selectedItem)
            trip_form_chipGroup.addView(chip as View)

            tripDestinations[destinationsCount] = TripDestinationCommand(
                type = 1,
                mountainId = selectedItem.mountainId,
                rockId = null
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedItem.latitude, selectedItem.longitude),7.5f))
            val marker = googleMap.setUpMarker(selectedItem.latitude, selectedItem.longitude, selectedItem.name, requireContext())
            markersMap[destinationsCount] = marker

            tripForm_searchView.text.clear()
            destinationsCount++
        }
    }

    private fun createChip(mountain: Mountain) : Chip {
        val chip = Chip(context)
        chip.text = mountain.name+" ("+mountain.regionName+", "+mountain.metersAboveSeaLevel+" m n.p.m.)"
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            val viewRowIndex = trip_form_chipGroup.indexOfChild(it)
            tripDestinations.remove(viewRowIndex)
            val marker = markersMap[viewRowIndex]
            marker?.remove()
            markersMap.remove(viewRowIndex)
            trip_form_chipGroup.removeView(it)
            destinationsCount--
        }
        return chip
    }

    private fun initViewModels() {
        viewModel = ViewModelProviders.of(this, TripFormViewModelFactory(requireContext())).get(TripFormViewModel::class.java)
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
