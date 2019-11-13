package com.hiker.presentation.mountainObjects.detailsTabs.mountainInformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiker.R
import com.hiker.data.remote.dto.MountainTrail
import com.hiker.presentation.trips.tabViews.Trip
import kotlinx.android.synthetic.main.fragment_mountain_information_view.*


private const val ARG_TRAILS = "ARG_TRAILS"

class MountainInformationTabView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mountain_information_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val trails = listOf<Trail>(Trail(1, 2.5f, "blue"), Trail(2, 3.15f, "red"), Trail(2, 3.15f, "red"))
        arguments?.let{
            val trails = it.getParcelableArrayList<MountainTrail>(ARG_TRAILS)
            if (trails != null){
                mountain_information_trailsList.layoutManager = LinearLayoutManager(activity)
                mountain_information_trailsList.adapter = TrailAdapter(requireContext(), trails.map { t -> Trail(t.id,t.timeToTopMinutes/60,t.color) })
            }
        }

    }

    companion object {
        @JvmStatic
        fun create(trails: ArrayList<MountainTrail>) =
            MountainInformationTabView().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_TRAILS, trails)
                }
            }
    }
}