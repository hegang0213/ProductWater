package com.bdwater.productwater.model.asset

import java.util.*

/**
 * Created by hegang on 2018/8/7.
 */
class User {
    var userId = ""
    var name = ""
    var userName = ""
    var comment = ""
    var lastLogonDate : Date? = null
    var sites: Array<Site> = emptyArray()
    var permissionGrants: Array<PermissionGrant> = emptyArray()
    fun getDefaultSiteName(): String {
        if(sites.isEmpty()) return ""
        return sites[0].name;
    }
}