package com.bdwater.productwater.web

import okhttp3.Call

/**
 * Created by hegang on 2018/8/7.
 */
interface GeneralCallback {
    fun onError(call: Call?, e: Exception?, id: Int)
    fun onResponse(jsonString: String, result: WebHelper.JsonResult, id: Int)
    fun onLocalError(result:String, e: Exception)
    fun onFinally()
}