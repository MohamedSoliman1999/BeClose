package com.example.beclose.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val fragmentTitle: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    fun insertFragment(fragment: Fragment, title: String) {
        fragmentTitle.add(title)
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun resetAdapter() {
        fragmentList.clear()
        fragmentTitle.clear()
    }

}