package com.bdwater.productwater.asset.device


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bdwater.productwater.CustomApplication

import com.bdwater.productwater.R
import com.bdwater.productwater.common.DataLoader
import com.bdwater.productwater.model.asset.ColumnDescription
import com.bdwater.productwater.model.asset.Device
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import okhttp3.Call


/**
 * A simple [Fragment] subclass.
 */
class DeviceFragment() : Fragment(), DataLoader {
    companion object {
        fun newInstance(device: Device, loadFrom: Boolean = true) : DeviceFragment {
            var fragment = DeviceFragment()
            fragment.device = device
            fragment.loadFrom = loadFrom
            return fragment
        }
    }
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar
    @BindView(R.id.nameTextView) lateinit var nameTextView: TextView
    @BindView(R.id.installDateTextView) lateinit var installDateTextView: TextView
    @BindView(R.id.addressTextView) lateinit var addressTextView: TextView
    @BindView(R.id.childrenTitleLayout) lateinit var childrenTitleLayout: LinearLayout
    @BindView(R.id.childrenBodyLayout) lateinit var childrenBodyLayout: LinearLayout
    @BindView(R.id.childrenBodyContentLayout) lateinit var childrenBodyContentLayout: LinearLayout
    @BindView(R.id.columnLayout) lateinit var columnLayout: LinearLayout

    private lateinit var device : Device
    private var loadFrom = true;
    private lateinit var rootView: View
    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater!!.inflate(R.layout.fragment_device, container, false)
        this.unbinder = ButterKnife.bind(this, rootView)
        this.init()
        if(this.loadFrom)
            this.load()             // load device from network
        else
            this.bindViewModal()    // load device from local
        return this.rootView;
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun init() {
        progressBar.visibility = View.GONE
        nameTextView.text = device.name;
        installDateTextView.text = device.installDate
        addressTextView.text = device.address
        childrenTitleLayout.setOnClickListener {
            var expanding = it.tag != null
            childrenBodyLayout.visibility = if(expanding) View.GONE else View.VISIBLE
            it.tag = if(it.tag == null) true else null
        }
        childrenBodyLayout.visibility = View.GONE
    }
    override fun load() {
        this.loading()
        this.onLoad()
    }

    override fun loading() {
        this.progressBar.visibility = View.VISIBLE
    }

    override fun onLoad() {
        WebHelper.WebService.Asset.getDevice()
                .addParameter("userID", CustomApplication.user.userId)
                .addParameter("deviceIDString", this.device.deviceId)
                .execute(object: GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        onError(e!!.message)
                    }

                    override fun onLocalError(result: String, e: Exception) {
                        Log.e(DeviceActivity.TAG, e.message)
                        onError(e!!.message)
                    }

                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        //Toast.makeText(application, jsonString, Toast.LENGTH_SHORT).show()
                        if(result.code != 0) {
                            onLocalError(jsonString, Exception(result.stackTrace))
                            return;
                        }
                        try {
                            device = Gson().fromJson(result.data, Device::class.java);
                            bindViewModal()
                        }
                        catch (e: Exception) {
                            onLocalError(jsonString, e)
                        }
                    }

                    override fun onFinally() {
                        this@DeviceFragment.onFinally()
                    }
                })
    }

    override fun onError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onFinally() {
        this.progressBar.visibility = View.GONE
    }
    private fun bindViewModal() {
        // shows default message if children is empty
        if(!this.device.children.isEmpty()) {
            // has children, create content of children
            this.childrenBodyContentLayout.removeAllViews()
            this.createChildren(this.device, 0)
        }
        this.createColumns();
    }
    // create sub devices into body content layout
    private fun createChildren(parent: Device, level: Int) {
        val indent = resources.getDimensionPixelSize(R.dimen.propertyIndent);

        // create sub devices
        for (child: Device in parent.children) {
            val layout = layoutInflater.inflate(DeviceActivity.SUB_LAYOUT, null)
            layout.findViewById<TextView>(R.id.nameTextView).text = child.name;
            layout.setPadding((level + 1) * indent, indent, indent, indent)
            this.childrenBodyContentLayout.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            this.createChildren(child, level + 1)
            layout.setOnClickListener {
                var jsonString = Gson().toJson(child)
                var intent = Intent(activity, DeviceActivity::class.java)
                intent.putExtra(DeviceActivity.Args.DEVICE, jsonString)
                intent.putExtra(DeviceActivity.Args.LOAD_FROM, false)
                startActivity(intent)
            }
        }
    }
    private fun createColumns() {
        for (column: ColumnDescription in this.device.columns) {
            val layout = layoutInflater.inflate(DeviceActivity.COLUMN_LAYOUT, null)
            layout.findViewById<TextView>(R.id.nameTextView).text = column.title;
            layout.findViewById<TextView>(R.id.valueTextView).text = column.value;
            this.columnLayout.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}// Required empty public constructor
