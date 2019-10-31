package com.hiker.presentation.trips.tripDetails


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.hiker.R
import com.hiker.presentation.login.LoginViewModel
import com.hiker.presentation.login.LoginViewModelFactory
import com.hiker.presentation.map.MapViewModel
import com.hiker.presentation.map.MapViewModelFactory
import kotlinx.android.synthetic.main.fragment_trip_details_view.*

/**
 * A simple [Fragment] subclass.
 */
class TripDetailsView : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMapView : com.google.android.gms.maps.MapView
    private lateinit var lastLocation: Location
    private lateinit var tripsDetailsViewModel: TripDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trip_details_view, container, false)
        googleMapView = view.findViewById(R.id.trip_details_mapView)
        googleMapView.onCreate(savedInstanceState)
        googleMapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        arguments?.let {
            val safeArgs = TripDetailsViewArgs.fromBundle(it)
            trip_details_view_tripTitle_textView.text = safeArgs.tripTitle
            trip_details_view_dateFrom_textView.text = safeArgs.tripDateFrom
            trip_details_view_dateTo_textView.text = safeArgs.tripDateTo
            tripsDetailsViewModel.getTrip(safeArgs.tripId).observe(this, Observer { trip ->
                trip_details_description.text = trip.description
                trip_details_participantsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                trip_details_participantsList.adapter = UserBriefAdapter((trip.tripParticipants!!.map { u -> UserBrief(u.id.toString(), u.firstName!!, u.lastName!!, u.profilePictureUrl!!)} + UserBrief(trip.author.id.toString(), trip.author.firstName!!, trip.author.lastName!!, trip.author.profilePictureUrl!!)))
            })
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        setUpMap(map!!)
    }

    private fun setUpMap(map: GoogleMap) {
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 7.5f))
            }
        }
    }

    private fun initViewModel() {
        tripsDetailsViewModel= ViewModelProviders.of(this, TripDetailsViewModelFactory()).get(TripDetailsViewModel::class.java)
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
