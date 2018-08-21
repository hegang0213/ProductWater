package com.bdwater.productwater.asset.device

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arlib.floatingsearchview.FloatingSearchView
import com.bdwater.productwater.CustomApplication
import com.bdwater.productwater.R
import com.bdwater.productwater.common.DataLoader
import com.bdwater.productwater.common.SwipeActivity
import com.bdwater.productwater.main.AssetFragment
import com.bdwater.productwater.main.DeviceListRecyclerViewAdapter
import com.bdwater.productwater.model.asset.Device
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

import kotlinx.android.synthetic.main.activity_device_list.*
import okhttp3.Call
import java.lang.Exception

class DeviceListActivity : SwipeActivity(), DataLoader {
    companion object {
        const val TAG = "DeviceListActivity"
        const val SIZE = 10
    }
    @BindView(R.id.refreshLayout) lateinit var refreshLayout: SmartRefreshLayout
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar
    @BindView(R.id.include_titleLayout) lateinit var titleLayout: LinearLayout
    @BindView(R.id.searchView) lateinit var searchView: LinearLayout
    @BindView(R.id.recyclerView) lateinit var recyclerView: SwipeMenuRecyclerView
    @BindView(R.id.errorLayout) lateinit var errorLayout: LinearLayout

    lateinit var unbinder: Unbinder
    private lateinit var adapter: DeviceListRecyclerViewAdapter
    private var loadMore = false
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)


        this.unbinder = ButterKnife.bind(this)
        this.init()
    }
    private fun init() {
        this.refreshLayout.setOnRefreshListener { this.load() }
        this.refreshLayout.setOnLoadMoreListener { this.loadMore() }
        this.progressBar.visibility = View.GONE
        this.titleLayout.findViewById<TextView>(R.id.include_titleTextView).text = resources.getText(R.string.device)
        this.searchView.setOnClickListener {
            val intent = Intent(this@DeviceListActivity, DeviceSearchActivity::class.java)
            startActivity(intent)
        }
        this.adapter = DeviceListRecyclerViewAdapter()
        this.recyclerView.layoutManager = LinearLayoutManager(this)
        this.recyclerView.adapter = adapter
        this.recyclerView.setSwipeItemClickListener { _, position ->
            var entry =  this.adapter.getItem(position)
            var jsonString = Gson().toJson(entry)
            val intent = Intent(this, DeviceActivity::class.java)
            intent.putExtra(DeviceActivity.Args.DEVICE, jsonString)
            startActivity(intent)
        }

        this.errorLayout.visibility = View.GONE
        this.errorLayout.setOnClickListener {
            if(loadMore) this.loadMore() else this.load()
        }
        this.load()
    }

    private fun loadMore() {
        loadMore = true
        this.loading()
        this.onLoad()
    }
    override fun load() {
        loadMore = false
        this.loading()
        this.onLoad()
    }

    override fun onLoad() {
        if(this.loading) return;
        this.loading()

        var offset = if(loadMore) adapter.devices.size else 0
        WebHelper.WebService.Asset.getDevices()
                .addParameter("userId", CustomApplication.user.userId)
                .addParameter("offset", "" + offset)
                .addParameter("size", "" + DeviceListActivity.SIZE)
                .execute(object : GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        this@DeviceListActivity.onError(e!!.message)
                    }
                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        var result = Gson().fromJson(result.data, Array<Device>::class.java)
                        if(!loadMore) adapter.devices.clear()
                        Log.e(DeviceListActivity.TAG, "original devices:" + adapter.devices.size)
                        adapter.devices.addAll(result)
                        Log.e(DeviceListActivity.TAG, "devices:" + adapter.devices.size)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onLocalError(result: String, e: Exception) {
                        this@DeviceListActivity.onError(e.message)
                    }

                    override fun onFinally() {
                        this@DeviceListActivity.onFinally()
                    }
                })
    }

    override fun loading() {
        if(!loadMore)
            this.progressBar.visibility = View.VISIBLE
        this.errorLayout.visibility = View.GONE
    }

    override fun onFinally() {
        this.refreshLayout.finishRefresh()
        this.refreshLayout.finishLoadMore()
        this.progressBar.visibility = View.GONE
    }

    override fun onError(message: String?) {
        errorLayout.visibility = View.GONE
        errorLayout.findViewById<TextView>(R.id.message).text = message
    }

}
