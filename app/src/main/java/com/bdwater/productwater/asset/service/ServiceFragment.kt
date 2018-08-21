package com.bdwater.productwater.asset.service


import android.bluetooth.BluetoothClass
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder

import com.bdwater.productwater.R
import com.bdwater.productwater.common.DataLoader
import com.bdwater.productwater.model.asset.Device
import com.bdwater.productwater.model.asset.Service
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import okhttp3.Call


/**
 * A simple [Fragment] subclass.
 */
class ServiceFragment : Fragment(), DataLoader {
    companion object {
        fun newInstance(device: Device): ServiceFragment {
            var fragment = ServiceFragment()
            fragment.device = device
            return fragment
        }
    }

    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar
    @BindView(R.id.noDataLayout) lateinit var noDataLayout: LinearLayout
    @BindView(R.id.swipeMenuRecyclerView) lateinit var recyclerView: SwipeMenuRecyclerView
    private lateinit var rootView: View
    private lateinit var unbinder: Unbinder
    private lateinit var device: Device
    private lateinit var adapter: ServiceRecyclerViewAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater!!.inflate(R.layout.fragment_service, container, false)
        this.unbinder = ButterKnife.bind(this, this.rootView)

        this.noDataLayout.visibility = View.GONE
        this.adapter = ServiceRecyclerViewAdapter();
        this.recyclerView.adapter = this.adapter
        this.load()
        return this.rootView;
    }

    override fun load() {
        this.loading()
        this.onLoad()
    }

    override fun loading() {
        this.progressBar.visibility = View.GONE
    }

    override fun onLoad() {
        WebHelper.WebService.Asset.getServices()
                .addParameter("deviceIDString", this.device.deviceId)
                .execute(object: GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        this@ServiceFragment.onError(e!!.message)
                    }

                    override fun onLocalError(result: String, e: Exception) {
                        this@ServiceFragment.onError(e.message)
                    }

                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        if(result.code != 0) {
                            onLocalError(jsonString, Exception(result.message))
                            return
                        }
                        var services = Gson().fromJson(result.data, Array<Service>::class.java)
                        adapter.services.clear()
                        adapter.services.addAll(services)
                        adapter.notifyDataSetChanged()
                        if(services.isEmpty())
                            noDataLayout.visibility = View.VISIBLE
                    }

                    override fun onFinally() {
                        this@ServiceFragment.onFinally()
                    }
                })
    }

    override fun onError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onFinally() {
        this.progressBar.visibility = View.GONE
    }

}// Required empty public constructor
