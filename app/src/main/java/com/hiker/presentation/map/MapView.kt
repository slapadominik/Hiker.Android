package com.hiker.presentation.map


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.hiker.R
import com.hiker.data.repository.MountainsRepositoryImpl
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat

import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiker.data.dto.Mountain
import kotlinx.android.synthetic.main.fragment_map_view.*


class MapView : Fragment(), OnMapReadyCallback {

    private lateinit var googleMapView: com.google.android.gms.maps.MapView
    private lateinit var googleMap : GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapViewModel : MapViewModel
    private lateinit var mountainCustomInfoWindow : ConstraintLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private val mountainsMarkers = HashMap<Marker, Mountain>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_map_view, container, false)

        mountainCustomInfoWindow = view.findViewById(R.id.mountain_info_window)
        googleMapView = view.findViewById(R.id.mapView2)
        googleMapView.onCreate(savedInstanceState)
        googleMapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        mountainCustomInfoWindow.visibility = View.INVISIBLE
        bottomNavigationView.visibility = View.VISIBLE
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMapViewModel()
        bindMountainsToMap()
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setUpMap()
        bindMountainsToMap()
        setUpMountainInfoWindow(googleMap)
    }

    private fun bindMountainsToMap(){
        mapViewModel.getAllMountains().observe(this, Observer { mountains ->
            mountains.forEach { mountain -> setUpMarker(mountain) }
        })
    }

    private fun setUpMarker(mountain: Mountain){
        val marker = googleMap.addMarker(MarkerOptions()
            .position(LatLng(mountain.location.latitude, mountain.location.longitude))
            .title(mountain.name)
            .icon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_marker_pin_0_trips)))
        mountainsMarkers[marker] = mountain

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 7.5f))
            }
        }
    }

    private fun setUpMountainInfoWindow(map : GoogleMap){
        map.setOnMapClickListener {
            mountainCustomInfoWindow.visibility = View.INVISIBLE

            bottomNavigationView.visibility = View.VISIBLE
        }
        map.setOnMarkerClickListener { marker ->
            val mountain = mountainsMarkers[marker]
            if (mountain != null) {
                marker_object_name.text = mountain.name
                marker_object_regionName.text = mountain.location.regionName
                marker_object_metersAboveSeaLevel.text = mountain.metersAboveSeaLevel.toString()
            }
            mountainCustomInfoWindow.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.INVISIBLE
            mountain_details_button.setOnClickListener { btn ->
                findNavController().navigate(R.id.action_mapView_to_mountainDetailsView)
            }
            true
        }
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

    private fun initMapViewModel() {
        mapViewModel = ViewModelProviders.of(this, MapViewModelFactory(MountainsRepositoryImpl())).get(MapViewModel::class.java)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        fun newInstance() = MapView()
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
