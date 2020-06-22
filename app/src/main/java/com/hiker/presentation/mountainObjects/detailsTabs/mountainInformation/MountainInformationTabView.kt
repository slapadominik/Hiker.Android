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
private const val ARG_DESCRIPTION = "ARG_DESCRIPTION"

class MountainInformationTabView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mountain_information_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            val trails = it.getParcelableArrayList<MountainTrail>(ARG_TRAILS)
            val description = it.getString(ARG_DESCRIPTION)
            if (trails != null){
                mountain_information_trailsList.layoutManager = LinearLayoutManager(activity)
                val adapter = TrailAdapter(requireContext(), trails.map { t -> Trail(t.id, t.timeToTopMinutes.toInt(),t.color) })
                mountain_information_trailsList.adapter = adapter
            }
            if (description != null){
                mountain_information_description.text = description
            }
        }

    }

    companion object {
        @JvmStatic
        fun create(trails: ArrayList<MountainTrail>, description: String) =
            MountainInformationTabView().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_TRAILS, trails)
                    putString(ARG_DESCRIPTION, description)
                }
            }
    }
}