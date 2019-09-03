package com.hiker.presentation.mountains


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_mountain_details_view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MountainDetailsView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mountain_details_view, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentAdapter = MountainDetailsViewPagerAdapter(childFragmentManager)
        mountain_details_viewpager.adapter = fragmentAdapter
        mountain_details_tablayout.setupWithViewPager(mountain_details_viewpager)

        arguments?.let {
            val safeArgs = MountainDetailsViewArgs.fromBundle(it)
            val mountainId = safeArgs.mountainId
            setBasicMountainInfo(safeArgs.mountainName, safeArgs.regionName, safeArgs.metersAboveSea)
        }
    }

    private fun setBasicMountainInfo(mountainName: String, regionName: String, metersAboveSeaLevel: Int){
        mountainDetailsView_regionName.text = regionName
        mountainDetailsView_title.text = mountainName
        mountainDetailsView_metersAboveSea.text = metersAboveSeaLevel.toString()
    }


}
