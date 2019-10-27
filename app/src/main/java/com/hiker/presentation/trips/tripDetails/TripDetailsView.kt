package com.hiker.presentation.trips.tripDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiker.R
import com.hiker.presentation.mountains.MountainDetailsViewArgs
import kotlinx.android.synthetic.main.fragment_trip_details_view.*

/**
 * A simple [Fragment] subclass.
 */
class TripDetailsView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_details_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = TripDetailsViewArgs.fromBundle(it)
            trip_details_view_tripTitle_textView.text = safeArgs.tripTitle
            trip_details_view_dateFrom_textView.text = safeArgs.tripDateFrom
            trip_details_view_dateTo_textView.text = safeArgs.tripDateTo
        }
    }

}
