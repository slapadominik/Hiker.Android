package com.hiker.presentation.map


import android.Manifest
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.hiker.R
import android.provider.BaseColumns
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.domain.entities.Status
import com.hiker.domain.extensions.*
import com.hiker.presentation.login.LoginViewModel
import com.hiker.presentation.login.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_map_view.*

const val MapViewBundleKey = "MapViewBundleKey"

class MapView : Fragment(), OnMapReadyCallback {

    private lateinit var googleMapView: com.google.android.gms.maps.MapView
    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapViewModel : MapViewModel
    private lateinit var mountainCustomInfoWindow : ConstraintLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var searchView: SearchView

    private var menu: Menu? = null
    private var userLocation: Location? = null
    private var circle: Circle? = null
    private val mountainsMarkers = HashMap<Marker, MountainBrief>()
    private val markersMountainIds = HashMap<Int, Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finishAffinity(requireActivity())
        }
    }
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
        mountainsMarkers.clear()
        markersMountainIds.clear()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showToolbarMenu()
        initViewModels()
        setUpOnFilterButtonClick()
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setUpMap()
        fetchMountains()
        setUpMountainInfoWindow(googleMap)
    }

    private fun fetchMountains(){
        mapViewModel.getMountains().observe(this, Observer { response ->
            if (response.status == Status.SUCCESS){
                response.data!!.forEach { mountain ->
                    val marker = googleMap.setUpMarker(mountain.location.latitude, mountain.location.longitude, mountain.name, requireContext())
                    mountainsMarkers[marker] = mountain
                    markersMountainIds[mountain.id] = marker
                }
                mapViewModel.cacheMountains(response.data)
            }
            else {
                val snack = Snackbar.make(requireView(), response.message!!, Snackbar.LENGTH_LONG)
                snack.anchorView = coordinatorLayout
                snack.show()
            }
        })
    }

    private fun setUpOnFilterButtonClick(){
        mapview_search_trip_button.setOnClickListener{
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_scan_map_trips, null)
            val dialog = Dialog(requireContext())
            dialog.setContentView(dialogView)
            dialog.show()
            val slider = dialog.findViewById<Slider>(R.id.dialog_slider_radius)
            val radiusText = dialog.findViewById<TextView>(R.id.dialog_radius_text)
            val cancelButton = dialog.findViewById<Button>(R.id.dialog_cancel_button)
            val submitButton = dialog.findViewById<Button>(R.id.dialog_submit_button)
            slider.setOnChangeListener{slider1, value ->
                radiusText.text = value.toInt().toString() +" km"
            }
            cancelButton.setOnClickListener{
                dialog.hide()
            }

            submitButton.setOnClickListener{
                if (userLocation!=null){
                    if (circle != null){
                        circle!!.remove()
                    }
                    mountainsMarkers.keys.forEach{marker -> marker.setNormalStyle(requireContext())}
                    val radiusKilometers = slider.value
                    val circleOptions = CircleOptions()
                        .center(LatLng(userLocation!!.latitude, userLocation!!.longitude))
                        .radius(radiusKilometers*1000.0)
                        .strokeWidth(2.0f)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#400084d3"))

                    circle = googleMap.addCircle(circleOptions)
                    mapViewModel.getMountaintsWithUpcomingTripsByRadius(
                        userLocation!!.latitude,
                        userLocation!!.longitude,
                        radiusKilometers.toInt()).observe(requireActivity(),Observer {
                        it.forEach{mountain ->
                            val marker = markersMountainIds[mountain.id]
                            marker!!.setFilteredStyle(requireContext())
                        }
                        var text = "Brak szczytów z wycieczkami"
                        if (!it.isEmpty()){
                            text = "Znaleziono ${it.count()} ${getPluralText(it.count(), "szczyt", "szczyty", "szczytów")} z wycieczkami"
                        }
                        Snackbar.make(relativeLayout,text, Snackbar.LENGTH_LONG)
                            .setAnchorView(coordinatorLayout)
                            .show();
                    })
                    dialog.hide()
                }

            }
        }
    }



    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                userLocation = location
                moveCamera(location.latitude, location.longitude, 7.5f)
            }
        }
    }

    private fun hideMenuToolbar(){
        val menuItem = menu?.findItem(R.id.action_search)
        menuItem?.collapseActionView()
    }

    private fun showToolbarMenu() {
        val toolbar = view?.findViewById<Toolbar>(R.id.mapview_toolbar)
        toolbar?.inflateMenu(R.menu.search_menu)
        menu = toolbar?.menu
        searchView =  toolbar?.menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(android.R.id.text1)
        val cursorAdapter = SimpleCursorAdapter(context, android.R.layout.simple_dropdown_item_1line, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard()
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val mountainName = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                val mountainId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                searchView.setQuery(mountainName, false)
                val it = mapViewModel.getMountain(mountainId)
                animateCamera(it.latitude, it.longitude, 9f)
                setMountainWindowData(it.mountainId, it.name, it.regionName, it.metersAboveSeaLevel, it.upcomingTripsCount)
                showMountainWindow()
                return true
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(requireContext(), query, Toast.LENGTH_LONG).show()
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null){
                    val searchText = "%$query%"
                    mapViewModel.getMountainsByName(searchText).observe(requireActivity(), Observer {
                        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                        it.forEach{  mountain ->
                            if (mountain.name.contains(query, true))
                                cursor.addRow(arrayOf(mountain.mountainId, mountain))
                        }
                        cursorAdapter.changeCursor(cursor)
                    })
                }
                return true
            }
        })
    }

    private fun setUpMountainInfoWindow(map : GoogleMap){
        map.setOnMapClickListener {
            hideMountainWindow()
            hideMenuToolbar()
            hideKeyboard()
        }
        map.setOnMarkerClickListener { marker ->
            val mountain = mountainsMarkers[marker]
            if (mountain != null) {
                setMountainWindowData(mountain.id, mountain.name, mountain.location.regionName, mountain.metersAboveSeaLevel, mountain.upcomingTripsCount)
                animateCamera(mountain.location.latitude, mountain.location.longitude, 9f)
                showMountainWindow()
            }
            true
        }
    }

    private fun hideMountainWindow(){
        mountainCustomInfoWindow.visibility = View.INVISIBLE
        bottomNavigationView.visibility = View.VISIBLE
        if (mapview_search_trip_button != null){
            mapview_search_trip_button.show()
        }
    }

    private fun showMountainWindow(){
        mountainCustomInfoWindow.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.INVISIBLE
        if ( mapview_search_trip_button != null){
            mapview_search_trip_button.hide()
        }
    }

    private fun setMountainWindowData(mountainId: Int, name: String, regionName: String, metersAboveSeaLevel: Int, upcomingTripsCount: Int){
        if (marker_object_name != null && marker_object_regionName != null && marker_object_metersAboveSeaLevel != null){
            marker_object_name.text = name
            marker_object_regionName.text = regionName
            marker_object_metersAboveSeaLevel.text = metersAboveSeaLevel.toString()
            marker_object_tripsCount.text = upcomingTripsCount.toString()
            mountain_details_button.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.mapView) {
                    val action = MapViewDirections.actionMapViewToMountainDetailsView()
                    action.mountainId = mountainId
                    action.mountainName = name
                    action.regionName = regionName
                    action.metersAboveSea = metersAboveSeaLevel
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun animateCamera(latitude: Double, longitude: Double, zoom: Float){
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom)
        googleMap.animateCamera(cameraUpdate)
    }

    private fun moveCamera(latitude: Double, longitude: Double, zoom: Float){
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom)
        googleMap.moveCamera(cameraUpdate)
    }

    private fun initViewModels() {
        mapViewModel = ViewModelProviders.of(this, MapViewModelFactory(requireContext())).get(MapViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(LoginViewModel::class.java)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onPause() {
        super.onPause()
        googleMapView.onPause()
    }
    override fun onDestroyView() {
        googleMap.clear()
        googleMapView.onDestroy()
        super.onDestroyView()
    }
    override fun onResume() {
        super.onResume()
        googleMapView.onResume()
        hideMenuToolbar()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        googleMapView.onLowMemory()
    }
}
