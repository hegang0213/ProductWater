package com.bdwater.productwater.asset.device

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
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
import okhttp3.Call
import java.lang.Exception

class DeviceSearchActivity : SwipeActivity(), DataLoader {
    companion object {
        const val TAG = "DeviceSearchActivity"
    }

    @BindView(R.id.refreshLayout) lateinit var refreshLayout: SmartRefreshLayout
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar
    @BindView(R.id.searchView) lateinit var searchView: FloatingSearchView
    @BindView(R.id.recyclerView) lateinit var recyclerView: SwipeMenuRecyclerView

    lateinit var unbinder: Unbinder
    private lateinit var adapter: DeviceListRecyclerViewAdapter
    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_search)

        this.unbinder = ButterKnife.bind(this)
        this.init()
    }

    private fun init() {
        this.refreshLayout.setOnRefreshListener { this.load() }
        this.refreshLayout.isEnableLoadMore = false
        this.adapter = DeviceListRecyclerViewAdapter()
        this.recyclerView.layoutManager = LinearLayoutManager(this)
        this.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        this.recyclerView.adapter = adapter

        this.progressBar.visibility = View.GONE
        this.searchView.setOnSearchListener(object: FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                if(currentQuery.isNullOrEmpty()) return
                this@DeviceSearchActivity.keyword = currentQuery as String
                this@DeviceSearchActivity.load()
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun load() {
        this.loading()
        this.onLoad()
    }

    override fun onLoad() {
        //var offset = if(loadMore) adapter.devices.size else 0
        WebHelper.WebService.Asset.getDevicesBySearch()
                .addParameter("userId", CustomApplication.user.userId)
                .addParameter("keyword", "" + this.keyword)
                .execute(object : GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        this@DeviceSearchActivity.onError(e!!.message)
                    }
                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        var result = Gson().fromJson(result.data, Array<Device>::class.java)
                        adapter.devices.clear()
                        Log.e(DeviceSearchActivity.TAG, "original devices:" + adapter.devices.size)
                        adapter.devices.addAll(result)
                        Log.e(DeviceSearchActivity.TAG, "devices:" + adapter.devices.size)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onLocalError(result: String, e: Exception) {
                        this@DeviceSearchActivity.onError(e!!.message)
                    }

                    override fun onFinally() {
                        this@DeviceSearchActivity.onFinally()
                    }
                })
    }

    override fun loading() {
        this.progressBar.visibility = View.VISIBLE
    }

    override fun onFinally() {
        this.progressBar.visibility = View.GONE
    }

    override fun onError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }
}
