package com.bdwater.productwater.model.asset

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by hegang on 2018/8/7.
 */
class Site {
    @SerializedName("SiteID")
    var siteId = ""
    @SerializedName("ParentID")
    var parentId = ""
    @SerializedName("Name")
    var name = ""
    @SerializedName("Level")
    var level: Int = 0
}