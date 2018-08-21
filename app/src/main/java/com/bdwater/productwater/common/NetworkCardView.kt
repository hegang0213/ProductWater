package com.bdwater.productwater.common

import android.content.Context
import android.support.annotation.IntDef
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bdwater.productwater.R
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention

/**
 * Created by hegang on 2018/8/8.
 */
open class NetworkCardView : LinearLayout {
    protected lateinit var extView: CardView
    @CardViewTypeInt
    var viewType: Int = CardViewType.Loading
        set(value) {
            if (field != value) {
                field = value
                this.render()
            }
        }


    @BindView(R.id.titleTextView)
    lateinit var titleTextView: TextView
    @BindView(R.id.bodyTextView)
    lateinit var bodyTextView: TextView
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar
    @BindView(R.id.button)
    lateinit var retryButton: Button

    lateinit var unbinder: Unbinder

    val customLayoutParams: LinearLayout.LayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE

        internal val layout_res_id = R.layout.network_cardview

        @IntDef(VISIBLE.toLong(), GONE.toLong())
        @Retention(RetentionPolicy.SOURCE)
        annotation class Visibility

        @IntDef(CardViewType.Loading.toLong(), CardViewType.Exception.toLong(), CardViewType.EmptyData.toLong(), CardViewType.Message.toLong())
        @Retention(RetentionPolicy.SOURCE)
        annotation class CardViewTypeInt

        object CardViewType {
            const val Message: Int = 3
            const val EmptyData: Int = 2
            const val Exception: Int = 1
            const val Loading:Int = 0
        }
    }

    constructor(context: Context) : super(context) {
        this.initUI(null, null, -1)

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.initUI(context, attrs, -1)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initUI(context, attrs, defStyleAttr)
    }

    private fun initUI(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val inflater = LayoutInflater.from(this.context)
        extView = inflater.inflate(layout_res_id, null) as CardView
        unbinder = ButterKnife.bind(this, extView)
        if (context == null) return
        this.addView(extView, customLayoutParams)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkCardView)
        this.viewType = ta.getInt(R.styleable.NetworkCardView_viewType, CardViewType.Loading)
        ta.recycle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setTitleVisibility(@Visibility visibility: Int) {
        this.titleTextView.visibility = visibility
    }

    fun setTitle(cs: CharSequence) {
        if (cs == "")
            this.setTitleVisibility(GONE)
        else {
            this.titleTextView.text = cs
            this.setTitleVisibility(VISIBLE)
        }
    }

    fun setBody(cs: CharSequence) {
        this.bodyTextView.text = cs
    }

    fun setRetryButtonText(cs: CharSequence) {
        this.retryButton.text = cs
    }

    fun setRetryButtonVisibility(@Visibility visibility: Int) {
        this.retryButton.visibility = visibility
    }

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        val cardElevation = resources.getDimensionPixelOffset(R.dimen.cardElevation)
        (extView.layoutParams as LinearLayout.LayoutParams).setMargins(
                left + cardElevation,
                top + cardElevation,
                right + cardElevation,
                bottom + cardElevation)

    }

    fun setCardElevation(value: Float) {
        extView.cardElevation = value
    }



    fun loading() {
        this.visibility = View.VISIBLE
        this.viewType = CardViewType.Loading
    }

    fun exception(title: String, body: String) {
        this.visibility = View.VISIBLE
        this.viewType = CardViewType.Exception
        this.setTitle(title)
        this.setBody(body)
    }

    fun noData() {
        this.visibility = View.VISIBLE
        this.viewType = CardViewType.EmptyData
    }

    fun message(title: String, body: String) {
        this.visibility = View.VISIBLE
        this.viewType = CardViewType.Message
        this.setTitle(title)
        this.setBody(body)
    }

    fun finish() {
        this.visibility = View.GONE
    }

    internal fun render() {
        if (this.viewType == CardViewType.Loading) {
            titleTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
            bodyTextView.text = context.getString(R.string.loading)
        } else if (this.viewType == CardViewType.Exception) {
            titleTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            bodyTextView.visibility = View.VISIBLE
        } else if (this.viewType == CardViewType.Message) {
            titleTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.GONE
            bodyTextView.visibility = View.VISIBLE
        } else {
            titleTextView.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.GONE
            bodyTextView.visibility = View.VISIBLE
            bodyTextView.setText(R.string.no_data)
        }
    }



}
