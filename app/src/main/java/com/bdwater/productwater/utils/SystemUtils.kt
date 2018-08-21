package com.bdwater.productwater.utils

import android.content.Context
import android.content.pm.PackageInfo

/**
 * Created by hegang on 2018/8/7.
 */
object SystemUtils {
    private fun getPackageInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0);
    }
    fun getVersionCode(context: Context): Int {
        return getPackageInfo(context).versionCode;
    }
    fun getVersionName(context: Context): String {
        return getPackageInfo(context).versionName
    }
}