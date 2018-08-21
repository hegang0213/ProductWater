package com.bdwater.productwater.asset.device

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bdwater.productwater.R
import com.bdwater.productwater.common.SwipeActivity
import com.bdwater.productwater.model.asset.Device
import com.google.gson.Gson

class DeviceActivity : SwipeActivity() {
    object Args {
        const val DEVICE = "device"
        const val LOAD_FROM = "loadFrom"
    }
    companion object {
        const val TAG = "DeviceActivity"
        const val COLUMN_LAYOUT = R.layout.device_column;
        const val SUB_LAYOUT = R.layout.device_sub;
    }

    @BindView(R.id.viewPager) lateinit var viewPager: ViewPager
    @BindView(R.id.tabLayout) lateinit var tabLayout: TabLayout
    private lateinit var device: Device
    private lateinit var unbinder: Unbinder
    private var loadFrom = true;
    private lateinit var adapter: DeviceViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        this.unbinder = ButterKnife.bind(this)

        var jsonString = intent.getStringExtra(Args.DEVICE);
        if(intent.hasExtra(Args.LOAD_FROM))
            this.loadFrom = intent.getBooleanExtra(Args.LOAD_FROM, true)
        this.device = Gson().fromJson(jsonString, Device::class.java);
        this.adapter = DeviceViewPagerAdapter(supportFragmentManager, this, this.device, this.loadFrom)
        this.viewPager.adapter = this.adapter
        this.tabLayout.setupWithViewPager(viewPager)
        this.init()
    }

    private fun init() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

}
