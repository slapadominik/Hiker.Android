package com.hiker.presentation.mountainObjects

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MountainDetailsViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fragmentList:MutableList<Fragment> = ArrayList<Fragment>()
    val fragmentTitleList:MutableList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList.get(position)
    }

    fun addFragment(fragment:Fragment,title:String){
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}