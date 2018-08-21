package com.bdwater.productwater

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager
import com.bdwater.productwater.main.BottomNavigationAdapter
import com.bdwater.productwater.main.ViewPagerAdapter
import com.mikepenz.iconics.context.IconicsContextWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    @BindView(R.id.viewPager) lateinit var viewPager: ViewPager
    @BindView(R.id.bottomNavigation) lateinit var bottomNavigation: AHBottomNavigation

    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        unbinder = ButterKnife.bind(this)

        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_FULLSCREEN
//        window.statusBarColor = Color.WHITE
//        setSupportActionBar(toolbar)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter;


        // Bottom Navigation Adapter manages to initialize all items,
        // see code if you want to change item's content
        var navigationAdapter = BottomNavigationAdapter(this)
        navigationAdapter.setupWithBottomNavigation(bottomNavigation)
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottomNavigation.isBehaviorTranslationEnabled = false
        bottomNavigation.setOnTabSelectedListener{ position, _ ->
            viewPager.currentItem = position
            true;
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
