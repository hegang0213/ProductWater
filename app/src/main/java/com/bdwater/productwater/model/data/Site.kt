package com.bdwater.productwater.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by hegang on 2018/8/7.
 */
class Site {
    @SerializedName("SiteID")
    var siteId = ""
    @SerializedName("Name")
    var name = ""
    @SerializedName("DataTags")
    var dataTags: Array<DataTag> = emptyArray()
}

class DataTag {
    @SerializedName("DataTagID")
    var dataTagId = ""
    @SerializedName("Name")
    var name = ""
    @SerializedName("LastUpdateTime")
    var updateTime = ""
    @SerializedName("Value")
    var value = ""
}