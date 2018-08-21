package com.bdwater.productwater.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by hegang on 2018/8/7.
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
{
    private var fragments = ArrayList<Fragment>()
    init {
        fragments.add(AssetFragment.newInstance());
        fragments.add(ReportFragment.newInstance())
        fragments.add(DataFragment.newInstance());
    }
    override fun getItem(position: Int): Fragment {
        return fragments[position];
    }
    override fun getCount(): Int {
        return fragments.size;
    }
}