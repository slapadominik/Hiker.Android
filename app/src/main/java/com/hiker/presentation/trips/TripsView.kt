package com.hiker.presentation.trips


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_trip_form_view.*
import kotlinx.android.synthetic.main.fragment_trips_view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TripsView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_trips_view, container, false)
        var bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tripsView_viewPager.adapter = TripsViewPagerAdapter(childFragmentManager)
        tripsview_tablayout.setupWithViewPager(tripsView_viewPager)
    }


}
