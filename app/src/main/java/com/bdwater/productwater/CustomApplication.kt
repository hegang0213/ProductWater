package com.bdwater.productwater

import android.app.Application
import android.content.Context
import com.bdwater.productwater.model.asset.User
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.Iconics
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by hegang on 2018/8/9.
 */
class CustomApplication : Application() {
    companion object {
        var user: User = User()
    }

    override fun onCreate() {
        super.onCreate()
        user.userId = "6c431b71-528c-479a-8e23-6694c78f69cd"

        //only required if you add a custom or generic font on your own
        Iconics.init(applicationContext)

        //register custom fonts like this (or also provide a font definition file)
        Iconics.registerFont(CommunityMaterial())

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build()
        OkHttpUtils.initClient(okHttpClient)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(android.R.color.white, android.R.color.black);//全局设置主题颜色
            ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        };
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f);
        };
    }

}