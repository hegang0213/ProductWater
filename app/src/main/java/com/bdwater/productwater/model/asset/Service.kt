package com.bdwater.productwater.model.asset

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by hegang on 2018/8/16.
 */
class Service {
    @SerializedName("ServiceID")
    var serviceId = ""
    @SerializedName("Type")
    var type: Int = 0
    @SerializedName("ServiceDate")
    var serviceDate = ""
    @SerializedName("TypeName")
    var typeName = ""
    @SerializedName("Title")
    var title = ""
    @SerializedName("Content")
    var content = ""
    @SerializedName("BigRepairIndex")
    var bigRepairIndex: Int = 0
}