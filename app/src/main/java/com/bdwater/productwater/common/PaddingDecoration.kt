package com.bdwater.productwater.common

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

/**
 * Created by hegang on 2018/8/8.
 */
class PaddingDecoration(private val leftPadding: Int, private val topPadding: Int, private val rightPadding: Int, private val bottomPadding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val lp = view.layoutParams as RecyclerView.LayoutParams
        val position = lp.viewLayoutPosition
        if (position == 0) {
            outRect.set(leftPadding, topPadding, rightPadding, 0)
        } else if (position == parent.adapter.itemCount - 1) {
            outRect.set(leftPadding, 0, rightPadding, bottomPadding)
        } else {
            outRect.set(leftPadding, topPadding, rightPadding, 0)
        }
    }
}