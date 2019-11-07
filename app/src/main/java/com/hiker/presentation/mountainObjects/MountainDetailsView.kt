package com.hiker.presentation.mountainObjects


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiker.R
import com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation.MountainInformationTabView
import com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips.MountainTripsTabView
import kotlinx.android.synthetic.main.fragment_mountain_details_view.*

const val MountainTripDestinationType = 1

class MountainDetailsView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mountain_details_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = MountainDetailsViewArgs.fromBundle(it)
            val mountainId = safeArgs.mountainId
            setBasicMountainInfo(safeArgs.mountainName, safeArgs.regionName, safeArgs.metersAboveSea)
            setUpTabs(safeArgs.regionName, safeArgs.metersAboveSea, MountainTripDestinationType, mountainId)
        }
    }

    private fun setBasicMountainInfo(mountainName: String, regionName: String, metersAboveSeaLevel: Int){
        mountainDetailsView_regionName.text = regionName
        mountainDetailsView_title.text = mountainName
        mountainDetailsView_metersAboveSea.text = metersAboveSeaLevel.toString()
    }

    private fun setUpTabs(regionName: String, metersAboveSeaLevel: Int, tripDestinationType: Int, mountainId: Int){
        val fragmentAdapter = MountainDetailsViewPagerAdapter(childFragmentManager)
        fragmentAdapter.addFragment(MountainInformationTabView.create(regionName, metersAboveSeaLevel), "Informacje")
        fragmentAdapter.addFragment(MountainTripsTabView.create(tripDestinationType, mountainId, 0), "Wycieczki")
        mountain_details_viewpager.adapter = fragmentAdapter
        mountain_details_tablayout.setupWithViewPager(mountain_details_viewpager)
    }


}
