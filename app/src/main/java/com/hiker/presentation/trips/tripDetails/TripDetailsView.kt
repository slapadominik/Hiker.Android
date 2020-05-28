package com.hiker.presentation.trips.tripDetails


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiker.R
import com.hiker.data.converters.asDbModel
import com.hiker.data.converters.asDomainModel
import com.hiker.domain.consts.OperationType
import com.hiker.domain.consts.TripDestinationType
import com.hiker.domain.exceptions.ApiException
import com.hiker.domain.exceptions.TypeNotSupportedException
import com.hiker.domain.extensions.getWeekDayName
import com.hiker.presentation.user.tripParticipant.TripParticipantViewArgs
import kotlinx.android.synthetic.main.fragment_trip_details_view.*
import kotlinx.android.synthetic.main.fragment_trip_form_view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TripDetailsView : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMapView : com.google.android.gms.maps.MapView
    private lateinit var tripsDetailsViewModel: TripDetailsViewModel
    private val dateFormater  = SimpleDateFormat("yyyy-MM-dd")
    private val userBriefAdapter =  UserBriefAdapter {
        val action = TripDetailsViewDirections.actionTripDetailsViewToTripParticipantView(it.id)
        findNavController().navigate(action)
    }
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_details_view, container, false)
        googleMapView = view.findViewById(R.id.trip_details_mapView)
        googleMapView.onCreate(savedInstanceState)
        googleMapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        var bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.INVISIBLE
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpToolbarNavigationClick()
        trip_details_participantsList.layoutManager = LinearLayoutManager(activity)
        trip_details_participantsList.adapter = userBriefAdapter
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = UUID.fromString(sharedPref.getString(getString(R.string.preferences_userSystemId), null))
        arguments?.let {
            val safeArgs = TripDetailsViewArgs.fromBundle(it)
            setUpTextViews(safeArgs.tripTitle, safeArgs.tripDateFrom, safeArgs.tripDateTo, safeArgs.isOneDay)
            setUpJoinTripButton(safeArgs.tripId, userSystemId)
            setUpQuitTripButton(safeArgs.tripId, userSystemId)
            getTripDetails(safeArgs.tripId, userSystemId)
            getTripParticipants(safeArgs.tripId)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
    }

    private fun getTripParticipants(tripId: Int){
        try{
            tripsDetailsViewModel.getTripParticipants(tripId).observe(this, Observer {participants ->
                tripsDetailsViewModel.getUsersBriefs(participants.map { x -> x.userId }).observe(this, Observer{ users ->
                   userBriefAdapter.setData(users)
                })
            })
        }
        catch (ex: Exception){
            Log.e("TripDetailsView", ex.message)
        }
    }

    private fun getTripDetails(tripId: Int, userSystemId: UUID){
        try{
            tripsDetailsViewModel.getTrip(tripId).observe(this, Observer { trip ->
                if (trip.author.id == userSystemId && trip.dateFrom > Calendar.getInstance().time){
                   setUpToolbarMenu(trip.id, trip.tripTitle, trip.description)
                }
                if (!trip.tripParticipants.any { x -> x.id ==  userSystemId} && trip.author.id != userSystemId){
                    trip_details_joinTripButton.visibility = View.VISIBLE
                }
                if (trip.tripParticipants.any{x -> x.id == userSystemId}){
                    trip_details_quitTripButton.visibility = View.VISIBLE
                }
                trip_details_description.text = trip.description
                trip_destination_destinationsList.layoutManager = LinearLayoutManager(activity)
                trip_destination_destinationsList.adapter = TripDestinationAdapter(trip.tripDestinations!!.mapIndexed { index, td -> TripDestination(index,td.type, td.mountainBrief?.asDbModel(), td.rock?.asDomainModel()) })
                val destinationsLocations = trip.tripDestinations.map { x ->
                    if (x.type == TripDestinationType.Mountain){
                        LatLng(x.mountainBrief!!.location.latitude, x.mountainBrief.location.longitude)
                    }
                    else if (x.type == TripDestinationType.Rock){
                        LatLng(x.rock!!.location.latitude, x.rock.location.longitude)
                    }
                    else throw TypeNotSupportedException("Trip destination type ${x.type} not supported.")
                }
                setUpDestinationMarkers(googleMap, destinationsLocations)
            })
        }
        catch (ex: Exception){
            Log.e("TripDetailsView", ex.message)
        }
    }

    private fun setUpToolbarMenu(tripId: Int, tripTitle: String, tripDescription: String){
        val toolbar = view?.findViewById<Toolbar>(R.id.trip_details_toolbar)
        toolbar?.inflateMenu(R.menu.trip_details_menu)
        toolbar?.overflowIcon= ContextCompat.getDrawable(context!!, R.drawable.ic_more_vert_white_24dp)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.trip_details_menu_delete -> showDeleteAlertDialog(tripId)
                R.id.trip_details_menu_edit -> showEditTripView(tripId, tripTitle, tripDescription)
            }
            false
        }
    }

    private fun setUpToolbarNavigationClick(){
        val toolbar = view?.findViewById<Toolbar>(R.id.trip_details_toolbar)
        toolbar?.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun showDeleteAlertDialog(tripId: Int) : androidx.appcompat.app.AlertDialog{
        return androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Uwaga")
            .setMessage("Czy chcesz usunąć wycieczkę?")
            .setPositiveButton("Tak") { _, _ ->
                tripsDetailsViewModel.removeTrip(tripId)
                findNavController().popBackStack()
            }
            .setNegativeButton("Nie", /* listener = */ null)
            .show()
    }

    private fun showEditTripView(tripId: Int, tripTitle: String, tripDescription: String){
        val action = TripDetailsViewDirections.actionTripDetailsViewToTripFormView(OperationType.Edit, tripTitle, tripDescription, null, null, tripId)
        findNavController().navigate(action)
    }

    private fun setUpTextViews(tripTitle: String, tripDateFrom: String, tripDateTo: String, isOneDay: Boolean)
    {
        trip_details_view_tripTitle_textView.text = tripTitle
        trip_details_view_dateFrom_textView.text = tripDateFrom
        if (isOneDay){
            trip_details_view_minus.visibility = View.GONE
            trip_details_view_dateTo_textView.visibility = View.GONE
            val date = dateFormater.parse(tripDateFrom)
            trip_details_view_oneDay_textView.text = "("+date.getWeekDayName()+")"
        }
        else{
            trip_details_view_dateTo_textView.text = tripDateTo
        }
    }

    private fun setUpJoinTripButton(tripId: Int, userId: UUID){
        trip_details_joinTripButton.setOnClickListener{
            try{
                tripsDetailsViewModel.addTripParticipant(tripId, userId)
                trip_details_joinTripButton.visibility = View.GONE
                trip_details_quitTripButton.visibility = View.VISIBLE
            }
            catch (apiException: ApiException){
                Log.e("TripDetailsView", apiException.message.toString())
            }
        }
    }

    private fun setUpQuitTripButton(tripId: Int, userId: UUID){
        trip_details_quitTripButton.setOnClickListener{
            try{
                tripsDetailsViewModel.removeTripParticipant(tripId, userId)
                trip_details_joinTripButton.visibility = View.VISIBLE
                trip_details_quitTripButton.visibility = View.GONE
            }
            catch (apiException: ApiException){
                Log.e("TripDetailsView", apiException.message.toString())
            }
        }
    }

    private fun setUpDestinationMarkers(map: GoogleMap,locations: List<LatLng>){
        var markers = locations.map {  MarkerOptions()
            .position(LatLng(it.latitude, it.longitude))
            .icon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_marker_pin_0_trips))}
        markers.forEach{map.addMarker(it)}
        if (locations.isNotEmpty()){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 7.5f))
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun initViewModel() {
        tripsDetailsViewModel= ViewModelProviders.of(this, TripDetailsViewModelFactory(requireContext())).get(TripDetailsViewModel::class.java)
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
