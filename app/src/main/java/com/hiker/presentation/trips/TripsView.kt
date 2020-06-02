package com.hiker.presentation.trips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_trips_view.*


class TripsView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trips_view, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tripsView_viewPager.adapter = TripsViewPagerAdapter(childFragmentManager)
        tripsview_tablayout.setupWithViewPager(tripsView_viewPager)
    }
}
