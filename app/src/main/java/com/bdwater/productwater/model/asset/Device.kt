package com.bdwater.productwater.model.asset

import com.google.gson.annotations.SerializedName

/**
 * Created by hegang on 2018/8/7.
 */
class Device {
    @SerializedName("DeviceID")
    var deviceId = ""
    @SerializedName("ParentID")
    var parentId = ""
    @SerializedName("Name")
    var name = ""
    @SerializedName("BigRepairCount")
    var bigRepairCount = ""
    @SerializedName("NextBigRepairDaysRemain")
    var nextBigRepairDaysRemain = ""
    @SerializedName("Address")
    var address = ""
    var comment= ""
    var level: Int = 0
    @SerializedName("InstallDate")
    var installDate = ""

    var hasChild: Boolean = false

    @SerializedName("Columns")
    var columns: Array<ColumnDescription> = emptyArray()
    @SerializedName("Children")
    var children: Array<Device> = emptyArray()
}