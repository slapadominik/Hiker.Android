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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.hiker.data.remote.dto.MountainBrief
import com.hiker.domain.extensions.hideKeyboard
import com.hiker.domain.extensions.setUpMarker
import com.hiker.presentation.login.LoginViewModel
import com.hiker.presentation.login.LoginViewModelFactory
import kotlinx.android.synthetic.main.dialog_scan_map_trips.*
import kotlinx.android.synthetic.main.fragment_map_view.*
import kotlin.math.log

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
    private var userLocation: Location? = null
    private var circle: Circle? = null
    private val mountainsMarkers = HashMap<Marker, MountainBrief>()


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
        fetchMountaints()
        setUpMountainInfoWindow(googleMap)
    }

    private fun fetchMountaints(){
        mapViewModel.getMountains().observe(this, Observer { mountains ->
            mountains.forEach { mountain ->
                val marker = googleMap.setUpMarker(mountain.location.latitude, mountain.location.longitude, mountain.name, requireContext())
                mountainsMarkers[marker] = mountain
            }
            mapViewModel.cacheMountains(mountains)
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
                    val radiusKilometers = slider.value*1000.0
                    val circleOptions = CircleOptions()
                        .center(LatLng(userLocation!!.latitude, userLocation!!.longitude))
                        .radius(radiusKilometers)
                        .strokeWidth(2.0f)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#400084d3"))
                    circle = googleMap.addCircle(circleOptions)
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
                animateCamera(location.latitude, location.longitude, 7.5f)
            }
        }
    }

    private fun showToolbarMenu() {
        val toolbar = view?.findViewById<Toolbar>(R.id.mapview_toolbar)
        toolbar?.inflateMenu(R.menu.search_menu)
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
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val mountainName = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                val mountainId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                searchView.setQuery(mountainName, false)
                mapViewModel.getMountain(mountainId).observe(requireActivity(), Observer {
                    animateCamera(it.latitude, it.longitude, 9f)
                    setMountainWindowData(it.mountainId, it.name, it.regionName, it.metersAboveSeaLevel, it.upcomingTripsCount)
                    showMountainWindow()
                })
                hideKeyboard()
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
            searchView.clearFocus()
            searchView.setQuery("", true)
            hideKeyboard()
        }
        map.setOnMarkerClickListener { marker ->
            val mountain = mountainsMarkers[marker]
            if (mountain != null) {
                setMountainWindowData(mountain.id, mountain.name, mountain.location.regionName, mountain.metersAboveSeaLevel, mountain.upcomingTripsCount)
                animateCamera(mountain.location.latitude, mountain.location.longitude, 9f)
            }
            mountain_details_button.setOnClickListener {
                val action = MapViewDirections.actionMapViewToMountainDetailsView()
                if (mountain != null) {
                    action.mountainId = mountain.id
                    action.mountainName = mountain.name
                    action.regionName = mountain.location.regionName
                    action.metersAboveSea = mountain.metersAboveSeaLevel
                }
                findNavController().navigate(action)
            }
            showMountainWindow()

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
            mapViewModel.setMountainThumbnail(mountain_info_window_imageview, mountainId)
        }
    }

    private fun animateCamera(latitude: Double, longitude: Double, zoom: Float){
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom)
        googleMap.animateCamera(cameraUpdate)
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
    }
    override fun onLowMemory() {
        super.onLowMemory()
        googleMapView.onLowMemory()
    }
}
