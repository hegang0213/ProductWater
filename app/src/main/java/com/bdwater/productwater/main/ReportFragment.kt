package com.bdwater.productwater.main


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.ButterKnife
import butterknife.Unbinder

import com.bdwater.productwater.R
import com.bdwater.productwater.model.asset.Device
import com.bdwater.productwater.model.asset.Site
import com.bdwater.productwater.model.asset.User
import com.bdwater.productwater.web.GeneralCallback
import com.bdwater.productwater.web.WebHelper
import com.google.gson.Gson
import okhttp3.Call
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 */
class ReportFragment : Fragment() {
    companion object {
        private const val TAG = "AssetFragment"
        fun newInstance(): ReportFragment {
            return ReportFragment();
        }
    }
    lateinit var unbinder: Unbinder
    lateinit var viewContext: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.viewContext = inflater!!.inflate(R.layout.fragment_report, container, false)
        this.unbinder = ButterKnife.bind(this, viewContext)
        this.init()
        return this.viewContext;
    }

    override fun onDestroy() {
        unbinder.unbind()
        super.onDestroy()
    }

    private var user: User? = null;
    private var devices: Array<Device> = emptyArray()
    private fun init() {
        Log.d(ReportFragment.TAG, WebHelper.Url.Asset.url);
        this.getLoginButton().setOnClickListener{
            this.clearText()
            this.appendText("execute [login], waiting...\n")
            WebHelper.WebService.Asset.login()
                    .addParameter("username", getUserName())
                    .addParameter("password", getPassword())
                    .execute(object: GeneralCallback{
                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            appendText(e!!.message)
                        }

                        override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                            appendText(jsonString)
                            try {
                                user = Gson().fromJson(result.data, User::class.java)
                            }
                            catch (exception:Exception) {
                                onLocalError(jsonString, exception)
                            }
                        }

                        override fun onLocalError(result: String, e: Exception) {
                            appendText(e.message)
                        }

                        override fun onFinally() {
                            appendText("\n[login] finished.")
                        }
                    })
        }
        this.getDevicesButton().setOnClickListener{
            if(this.user == null) {
                Toast.makeText(context, "The user is null.", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            this.clearText()
            this.appendText("execute [get devices], waiting...\n")
            WebHelper.WebService.Asset.getDevices()
                    .addParameter("userID", this.user!!.userId)
                    .addParameter("offset", "0")
                    .addParameter("size", "10")
                    .execute(object: GeneralCallback{
                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            appendText(e!!.message)
                        }

                        override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                            appendText(jsonString)
                            try {
                                devices = Gson().fromJson(result.data, Array<Device>::class.java)
                                appendText("Device[] size:" + devices.size)
                                for(device: Device in devices) {
                                    appendText(device.name + ", columns[]:" + device.columns.size + ", children[]:" + device.children.size)
                                }
                            }
                            catch (exception: Exception) {
                                onLocalError(jsonString, exception)
                            }
                        }

                        override fun onLocalError(result: String, e: Exception) {
                            appendText(e.message)
                        }

                        override fun onFinally() {
                            appendText("\n[get devices] finished.")
                        }
                    })
        }
        this.getAllUserButton().setOnClickListener{
            this.clearText()
            this.appendText("execute [all user], waiting...\n")
            WebHelper.WebService.Asset.allUser()
                    .execute(object: GeneralCallback{
                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            appendText(e!!.message)
                        }

                        override fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int) {
                            appendText(jsonString)
                        }

                        override fun onLocalError(result: String, e: Exception) {
                            appendText(e.message)
                        }

                        override fun onFinally() {
                            appendText("\n[all user] finished.")
                        }
                    })
        }
        this.getScrollView().viewTreeObserver.addOnGlobalLayoutListener {
            this.getScrollView().post {
                getScrollView().fullScroll(View.FOCUS_DOWN);
            }
        }
    }
    private fun getUserName():String {
        return this.viewContext.findViewById<EditText>(R.id.usernameEditText).text.toString();
    }
    private fun getPassword():String {
        return this.viewContext.findViewById<EditText>(R.id.passwordEditText).text.toString();
    }
    private fun getLoginButton(): Button {
        return this.viewContext.findViewById(R.id.loginButton);
    }
    private fun getDevicesButton(): Button {
        return this.viewContext.findViewById(R.id.devicesButton);
    }
    private fun getAllUserButton(): Button {
        return this.viewContext.findViewById(R.id.allUserButton);
    }
    private fun getResultTextView(): TextView {
        return this.viewContext.findViewById<TextView>(R.id.resultTextView)
    }
    private fun getScrollView():ScrollView {
        return this.viewContext.findViewById(R.id.scrollView);
    }
    private fun clearText() {
        this.getResultTextView().text = "";
    }
    private fun appendText(s:String?) {
        if(s.isNullOrEmpty()) return;
        this.getResultTextView().append(s + "\n");
    }

}// Required empty public constructor
