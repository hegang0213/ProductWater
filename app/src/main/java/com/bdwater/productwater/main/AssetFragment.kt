package com.bdwater.productwater.main


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arlib.floatingsearchview.FloatingSearchView
import com.bdwater.productwater.CustomApplication

import com.bdwater.productwater.R
import com.bdwater.productwater.asset.device.DeviceActivity
import com.bdwater.productwater.asset.device.DeviceListActivity
import com.bdwater.productwater.asset.device.DeviceSearchActivity
import com.bdwater.productwater.model.asset.Device
import com.bdwater.productwater.model.asset.User
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import okhttp3.Call
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class AssetFragment : Fragment() {
    companion object {
        private const val TAG = "AssetFragment"
        private const val SIZE = 5
        fun newInstance(): AssetFragment {
            return AssetFragment();
        }
    }
    private lateinit var adapter: DeviceListRecyclerViewAdapter
    private lateinit var unbinder: Unbinder
    private lateinit var viewContext: View

    private var loading = false;

    @BindView(R.id.swipeMenuRecyclerView)
    lateinit var swipeMenuRecyclerView: SwipeMenuRecyclerView

    private lateinit var smartRefreshLayout: SmartRefreshLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.viewContext = inflater!!.inflate(R.layout.fragment_asset, container, false)
        this.smartRefreshLayout = this.viewContext as SmartRefreshLayout
        this.unbinder = ButterKnife.bind(this, viewContext)
        this.init()
        return this.viewContext;
    }

    override fun onDestroy() {
        unbinder.unbind()
        super.onDestroy()
    }

    private var user: User? = null;
    private fun init() {
        smartRefreshLayout.isEnableLoadMore = false
        smartRefreshLayout.setOnRefreshListener { this.load() }
        smartRefreshLayout.setOnLoadMoreListener { this.loadMore() }

        this.viewContext.findViewById<TextView>(R.id.include_titleTextView).text = resources.getText(R.string.menu_assert)
        this.viewContext.findViewById<LinearLayout>(R.id.search_fake_layout).setOnClickListener {
            val intent = Intent(activity, DeviceSearchActivity::class.java)
            startActivity(intent)
        }

        this.viewContext.findViewById<LinearLayout>(R.id.deviceBlock).setOnClickListener { startActivity(Intent(activity, DeviceListActivity::class.java)) }

        //var loadMoreView = DefinedLoadMoreView(context)
        //loadMoreView.setMargin(0,10, 0,10)
        swipeMenuRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = DeviceListRecyclerViewAdapter()
        swipeMenuRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        // .addItemDecoration(PaddingDecoration(0, 10, 0, 10))
        //swipeMenuRecyclerView.addFooterView(loadMoreView)
        //swipeMenuRecyclerView.setLoadMoreView(loadMoreView)
        swipeMenuRecyclerView.adapter = adapter
        swipeMenuRecyclerView.setSwipeItemClickListener { _, position ->
            var device = adapter.getItem(position)
            var jsonString = Gson().toJson(device);
            var intent = Intent(this.context, DeviceActivity::class.java);
            intent.putExtra(DeviceActivity.Args.DEVICE, jsonString)
            startActivity(intent)
        }

        this.smartRefreshLayout.isEnableRefresh = false
        this.load()
    }
    private fun loading() {
        this.loading = true;
        if(!this.smartRefreshLayout.isRefreshing)
            this.smartRefreshLayout.isEnableRefresh = false;
    }
    private fun completed(loadMore: Boolean) {
        this.loading = false;
        this.smartRefreshLayout.isEnableRefresh = true;
        if(loadMore)
            this.smartRefreshLayout.finishLoadMore()
        else
            this.smartRefreshLayout.finishRefresh()
    }
    private fun loadMore() {
        //this.loadData(true)
    }
    private fun load() {
        this.loadData()
    }
    private fun loadData(loadMore: Boolean = false) {
        if(this.loading) return;
        this.loading()

        var offset = if(loadMore) adapter.devices.size else 0
        Toast.makeText(context, "offset:%s, size:%s".format(offset ,SIZE), Toast.LENGTH_SHORT).show()
        WebHelper.WebService.Asset.getDevices()
                .addParameter("userId", CustomApplication.user.userId)
                .addParameter("offset", "" + offset)
                .addParameter("size", "" + SIZE)
                .execute(object : GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {

                    }
                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        var result = Gson().fromJson(result.data, Array<Device>::class.java)
                        if(!loadMore) adapter.devices.clear()
                        Log.e(TAG, "original devices:" + adapter.devices.size)
                        adapter.devices.addAll(result)
                        Log.e(TAG, "devices:" + adapter.devices.size)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onLocalError(result: String, e: Exception) {

                    }

                    override fun onFinally() {
                        completed(loadMore)
                    }
                })
    }



}// Required empty public constructor
