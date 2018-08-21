package com.bdwater.productwater.asset.device

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.bdwater.productwater.R
import com.bdwater.productwater.asset.service.ServiceFragment
import com.bdwater.productwater.model.asset.Device

/**
 * Created by hegang on 2018/8/16.
 */
class DeviceViewPagerAdapter(fm: FragmentManager, context: Context, device: Device, loadFrom: Boolean): FragmentPagerAdapter(fm) {
    private var context = context
    private var fragments = ArrayList<Fragment>()
    private var titles = arrayOf(R.string.device, R.string.service)
    init {
        this.fragments.add(DeviceFragment.newInstance(device, loadFrom))
        this.fragments.add(ServiceFragment.newInstance(device))
    }


    override fun getItem(position: Int): Fragment {
        return this.fragments[position]
    }

    override fun getCount(): Int {
        return this.fragments.size;
    }

    override fun getPageTitle(position: Int): CharSequence {
        return this.context.resources.getString(this.titles[position])
    }
}