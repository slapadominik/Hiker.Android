package com.hiker.presentation.mountains.detailsTabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_mountain_information_view.*


private const val ARG_REGION_NAME = "ARG_REGION_NAME"
private const val ARG_METERS_ABOVE_SEA = "ARG_METERS_ABOVE_SEA"

class MountainInformationTabView : Fragment() {
    private var regionName: String? = null
    private var metersAboveSea: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mountain_information_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            regionName = it.getString(ARG_REGION_NAME)
            metersAboveSea = it.getInt(ARG_METERS_ABOVE_SEA)
            setUpText(regionName!!, metersAboveSea)
        }
    }

    private fun setUpText(regionName: String, metersAboveSea: Int){
        mountainInformationView_regionName.text = regionName
        mountainInformationView_metersAboveSea.text = metersAboveSea.toString()
    }

    companion object {
        @JvmStatic
        fun create(regionName: String, metersAboveSea: Int) =
            MountainInformationTabView().apply {
                arguments = Bundle().apply {
                    putString(ARG_REGION_NAME, regionName)
                    putInt(ARG_METERS_ABOVE_SEA, metersAboveSea)
                }
            }
    }
}