package com.hiker.presentation.mountainObjects


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hiker.R
import com.hiker.data.remote.dto.MountainTrail
import com.hiker.domain.extensions.TAG
import com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation.MountainInformationTabView
import com.hiker.presentation.mountainObjects.detailsTabs.mountainTrips.MountainTripsTabView
import kotlinx.android.synthetic.main.fragment_mountain_details_view.*
import java.lang.IllegalArgumentException

const val MountainTripDestinationType = 1

class MountainDetailsView : Fragment() {

    private lateinit var mountainDetailsViewModel: MountainDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mountain_details_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        arguments?.let {
            val safeArgs = MountainDetailsViewArgs.fromBundle(it)
            val mountainId = safeArgs.mountainId
            if (mountainId<=0){
                Log.e(TAG, "MountainId is 0 in safeArgs")
                throw IllegalArgumentException("MountainId should be ")
            }
            setBasicMountainInfo(safeArgs.mountainName, safeArgs.regionName, safeArgs.metersAboveSea)
            getMountainDetails(MountainTripDestinationType, mountainId)
        }
    }

    private fun getMountainDetails(tripDestinationType: Int, mountainId: Int){
        mountainDetailsViewModel.getMountainDetails(mountainId).observe(this, Observer {
            mountain_details_imageSlider.sliderAdapter = SliderAdapter(requireContext(), it.mountainImages)
            setUpTabs(ArrayList(it.trails),tripDestinationType,mountainId)
        })
    }

    private fun setBasicMountainInfo(mountainName: String, regionName: String, metersAboveSeaLevel: Int){
        mountainDetailsView_regionName.text = regionName
        mountainDetailsView_title.text = mountainName
        mountainDetailsView_metersAboveSea.text = metersAboveSeaLevel.toString()
    }

    private fun setUpTabs(trails: ArrayList<MountainTrail>, tripDestinationType: Int, mountainId: Int){
        val fragmentAdapter = MountainDetailsViewPagerAdapter(childFragmentManager)
        fragmentAdapter.addFragment(MountainInformationTabView.create(trails), "Informacje")
        fragmentAdapter.addFragment(MountainTripsTabView.create(tripDestinationType, mountainId, 0), "Wycieczki")
        mountain_details_viewpager.adapter = fragmentAdapter
        mountain_details_tablayout.setupWithViewPager(mountain_details_viewpager)
    }

    private fun initViewModel(){
        mountainDetailsViewModel = ViewModelProviders.of(this, MountainDetailsViewModelFactory()).get(MountainDetailsViewModel::class.java)
    }
}
