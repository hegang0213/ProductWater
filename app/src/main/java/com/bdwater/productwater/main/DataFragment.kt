package com.bdwater.productwater.main


import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bdwater.productwater.R
import com.bdwater.productwater.common.DataLoader
import com.bdwater.productwater.model.data.Site
import com.bdwater.productwater.utils.IconicsUtil
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.IIcon
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.data_item.*
import okhttp3.*
import org.w3c.dom.Text
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.security.auth.callback.Callback


/**
 * A simple [Fragment] subclass.
 */
class DataFragment : Fragment(), DataLoader {
    lateinit var unbinder: Unbinder
    private lateinit var viewRoot : View;
    companion object {
        const val TAG = "DataFragment"
        const val INTERVAL : Long = 20000
        fun newInstance(): DataFragment {
            return DataFragment();
        }
    }

    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar
    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView
    @BindView(R.id.errorLayout) lateinit var errorLayout: LinearLayout
    private lateinit var adapter: DataRecyclerViewAdapter
    private var firstLoaded = false
    private var isLoading = false

    private lateinit var timer : Timer
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewRoot = inflater!!.inflate(R.layout.fragment_data, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);
        this.init();
        this.load()
        this.timer = Timer(true)
        return viewRoot;
    }
    private val holder = object: Handler() {
        override fun handleMessage(msg: Message?) {
            load();
        }
    }

    override fun onResume() {
        Log.e(TAG, "onResume")
        super.onResume()
        this.timer.schedule(object : TimerTask() {
            override fun run() {
                holder.sendEmptyMessage(0)
                Log.e(TAG, "timer is tick...")
            }
        }, INTERVAL, INTERVAL)
    }

    override fun onPause() {
        Log.e(TAG, "onPause")
        super.onPause()
        this.timer.cancel()
        this.timer.purge()
    }

    override fun onDestroy() {
        unbinder.unbind();
        super.onDestroy()
    }

    private fun init() {
        val icon = this.viewRoot.findViewById<ImageView>(R.id.include_icon)
        icon.setImageDrawable(
                IconicsUtil.getSmallIcon(context, CommunityMaterial.Icon.cmd_monitor))
        icon.visibility = View.VISIBLE
        this.viewRoot.findViewById<TextView>(R.id.include_titleTextView).text = resources.getText(R.string.menu_data)
        this.progressBar.visibility = View.GONE
        this.errorLayout.visibility = View.GONE

        this.adapter = DataRecyclerViewAdapter()
        this.recyclerView.layoutManager = LinearLayoutManager(context)
        this.recyclerView.adapter = this.adapter
        this.errorLayout.setOnClickListener {
            this.load()
        }


    }
    override fun load() {
        if(this.isLoading) return;
        this.isLoading = true
        this.loading()
        this.onLoad()
    }

    override fun onLoad() {
        Log.d(TAG, WebHelper.Url.Data.url);
        WebHelper.WebService.Data.getAll()
                .execute(object : GeneralCallback {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        this@DataFragment.onError(e!!.message)
                    }

                    override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                        if(result.code != 0) {
                            onLocalError(jsonString, Exception(result.message))
                        }
                        var sites = Gson().fromJson(result.data, Array<Site>::class.java)
                        adapter.sites.clear()
                        adapter.sites.addAll(sites)
                        adapter.notifyDataSetChanged()
                        this@DataFragment.firstLoaded = true
                    }

                    override fun onLocalError(result: String, e: Exception) {
                        this@DataFragment.onError(e.message)
                    }

                    override fun onFinally() {
                        this@DataFragment.onFinally()
                    }
                });
    }

    override fun loading() {
        this.progressBar.visibility = View.VISIBLE
        this.errorLayout.visibility = View.GONE
    }

    override fun onFinally() {
        this.isLoading = false
        this.progressBar.visibility = View.GONE
    }

    override fun onError(message: String?) {
        if(firstLoaded) return;
        this.errorLayout.visibility = View.VISIBLE
        this.errorLayout.findViewById<TextView>(R.id.message).text = message
    }



}// Required empty public constructor

