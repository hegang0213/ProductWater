package com.bdwater.productwater.common

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.bdwater.productwater.R
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

/**
 * Created by hegang on 2018/8/8.
 */
class DefinedLoadMoreView : NetworkCardView, SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {

    private var mLoadMoreListener: SwipeMenuRecyclerView.LoadMoreListener? = null

    constructor(context: Context) : super(context, null) {
        this.visibility = View.VISIBLE
        this.setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        minimumHeight = 50
    }

    private fun initUI(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val inflater = LayoutInflater.from(context)
        layoutParams = ViewGroup.LayoutParams(-1, -2)

        super.extView = inflater.inflate(layout_res_id, null) as CardView
        super.extView.radius = 0f
        val params = super.customLayoutParams
        //        params.setMargins(15, 5, 15, 5);

        ButterKnife.bind(this, super.extView)
        this.addView(super.extView, params)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkCardView)
        super.viewType = ta.getInt(R.styleable.NetworkCardView_viewType, CardViewType.Loading)
        ta.recycle()
        //super.render()
    }

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    override fun onLoading() {
        // 展示加载更多的动画和提示信息。
        super.loading()
    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        // 根据参数，显示没有数据的提示、没有更多数据的提示。
        // 如果都不存在，则都不用显示。
        if (dataEmpty) {
            super.noData()
            return
        }

        if (!hasMore) {
            super.message("", "没有更多的数据了")
            return
        }

        super.finish()
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    override fun onLoadError(errorCode: Int, errorMessage: String) {
        super.exception("", errorMessage)
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，此方法被调用，并传入listener。
     */
    override fun onWaitToLoadMore(loadMoreListener: SwipeMenuRecyclerView.LoadMoreListener) {
        this.mLoadMoreListener = loadMoreListener
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    override fun onClick(v: View) {
        if (mLoadMoreListener != null) mLoadMoreListener!!.onLoadMore()
    }

}
