package com.hiker.presentation.mountains

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hiker.presentation.mountains.detailsTabs.MountainInformationTabView
import com.hiker.presentation.mountains.detailsTabs.MountainTripsTabView

class MountainDetailsViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                MountainInformationTabView()
            }
            else -> MountainTripsTabView()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Informacje"
            else -> "Wycieczki"
        }
    }
}