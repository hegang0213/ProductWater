package com.bdwater.productwater.model.asset

import com.google.gson.annotations.SerializedName

/**
 * Created by hegang on 2018/8/7.
 */
class ColumnDescription {
    @SerializedName("Name")
    var name = ""
    @SerializedName("Title")
    var title = ""
    @SerializedName("Value")
    var value = ""
}