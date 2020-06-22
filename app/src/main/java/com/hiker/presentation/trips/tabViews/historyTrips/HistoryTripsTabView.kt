package com.hiker.presentation.trips.tabViews.historyTrips


import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hiker.R
import com.hiker.domain.entities.Status
import com.hiker.presentation.trips.TripsViewDirections
import com.hiker.presentation.trips.tabViews.Trip
import com.hiker.presentation.trips.tabViews.TripAdapter
import kotlinx.android.synthetic.main.fragment_history_trips_tab_view.*
import java.text.SimpleDateFormat
import java.util.*


class HistoryTripsTabView : Fragment() {

    private lateinit var historyTripsTabViewModel: HistoryTripsTabViewModel
    private val dateFormater  = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_trips_tab_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupTripsList()
    }

    private fun setupTripsList(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)
        historyTripsTabViewModel.getUserHistoryTripsBriefs(userSystemId!!).observe(this, Observer { response ->
            if (response.status == Status.SUCCESS){
                val tripsBriefs = response.data!!
                if (tripsBriefs.isEmpty()){
                    historyTripsView_emptyList_textView.visibility = View.VISIBLE
                    historyTripsView_recyclerView.visibility = View.GONE
                }
                else{
                    historyTripsView_emptyList_textView.visibility = View.GONE
                    historyTripsView_recyclerView.visibility = View.VISIBLE
                    historyTripsView_recyclerView.layoutManager = LinearLayoutManager(activity)
                    historyTripsView_recyclerView.adapter =
                        TripAdapter(tripsBriefs.map { x ->
                            Trip(
                                x.id,
                                x.tripTitle,
                                x.dateFrom,
                                x.dateTo,
                                x.isOneDay,
                                x.authorId == userSystemId
                            )
                        }, requireContext()) {
                            val action = TripsViewDirections.actionTripsViewToTripDetailsView()
                            action.tripId = it.id
                            action.tripTitle = it.title
                            action.tripDateFrom = dateFormater.format(it.dateFrom)
                            action.tripDateTo = dateFormater.format(it.dateTo)
                            findNavController().navigate(action)
                        }
                }
            }
            else{
                val snack = Snackbar.make(requireView(), response.message!!, Snackbar.LENGTH_LONG)
                val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
                snack.anchorView = bottomNav
                snack.show()
            }
        })
    }

    private fun initViewModel(){
        historyTripsTabViewModel = ViewModelProviders.of(this, HistoryripsTabViewModelFactory(requireContext())).get(HistoryTripsTabViewModel::class.java)
    }
}
