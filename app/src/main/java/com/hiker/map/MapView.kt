package com.hiker.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hiker.BaseFragment
import com.hiker.R
import com.hiker.toolbar.FragmentToolbar




class MapView : Fragment(), OnMapReadyCallback {


    private lateinit var mapView: com.google.android.gms.maps.MapView
    private lateinit var googleMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_map_view, container, false)

        mapView = view.findViewById(R.id.mapView2)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        activity?.actionBar?.setDisplayShowTitleEnabled(false)
        return view
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
