package com.hiker.presentation.trips

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hiker.presentation.trips.tabViews.HistoryTripsTabView
import com.hiker.presentation.trips.tabViews.UpcomingTripsTabView

class TripsViewPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Nadchodzące"
            else -> "Historia"
        }
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> UpcomingTripsTabView()
            else -> HistoryTripsTabView()
        }
    }

}