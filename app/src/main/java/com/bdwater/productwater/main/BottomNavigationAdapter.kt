package com.bdwater.productwater.main

import android.content.Context
import android.graphics.Color
import android.support.annotation.StringRes
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bdwater.productwater.R
import com.bdwater.productwater.utils.IconicsUtil
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.typeface.IIcon

/**
 * Created by hegang on 2018/7/4.
 */
class BottomNavigationAdapter constructor(var context: Context)  {
    private val TAG: String = "BottomNavigationAdapter"
    public val ASSERT: String = "Assert"
    public val REPORT: String = "Report"
    public val DATA: String = "Data"
    var items = mutableListOf<CustomItem>()
    init {
        Log.e(TAG, "Bottom Navigation Adapter is initializing...")
        addItem(ASSERT, R.string.menu_assert, CommunityMaterial.Icon.cmd_home_outline)
        addItem(REPORT, R.string.menu_report, CommunityMaterial.Icon.cmd_account_search)
        addItem(DATA, R.string.menu_data, CommunityMaterial.Icon.cmd_alert_outline)
        Log.e(TAG, "Bottom Navigation Adapter has been initialized.")
    }
    private fun addItem(name: String, @StringRes titleRes: Int, icon: IIcon) {
        var title = this.context.resources.getString(titleRes)
        var item = CustomItem(name, title, icon)
        items.add(item)
    }
    fun setupWithBottomNavigation(navigation: AHBottomNavigation) {
        items.forEach {
            var ahItem = AHBottomNavigationItem(it.title, IconicsUtil.getNormalIcon(this.context, it.icon), Color.BLACK)
            navigation.addItem(ahItem)
        }
    }


    class CustomItem constructor(var name: String, var title: String, var icon: IIcon){
        private val TAG: String = "BottomNavigationAdapter.CustomItem"
        init {
            println("$TAG -> name:$name, title:$title, icon:$icon has been initialized.")
        }
    }
}