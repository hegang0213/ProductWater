package com.bdwater.productwater.common

import android.content.Context
import com.mikepenz.iconics.context.IconicsContextWrapper
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

/**
 * Created by hegang on 2018/8/15.
 */
open class SwipeActivity: SwipeBackActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }
}