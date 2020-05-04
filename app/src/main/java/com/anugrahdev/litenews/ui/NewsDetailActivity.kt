package com.anugrahdev.litenews.ui

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.anugrahdev.litenews.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_news_detail.*


class NewsDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    var isShow = true
    var scrollRange: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val collapsingToolbar = collapsing_toolbar as SubtitleCollapsingToolbarLayout
        collapsingToolbar.setCollapsedTitleTextColor(getColor(R.color.colorLightGray))
        collapsingToolbar.setCollapsedSubtitleTextColor(getColor(R.color.colorLightGray))
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_ATOP)
        appbar.addOnOffsetChangedListener(this)

        initWebView()
        initData()


    }

    fun initData(){
        Glide.with(this)
            .load(intent.extras?.getString("img"))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(iv_img)


    }

    fun initWebView(){
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.webViewClient = WebViewClient()
        webView.loadUrl(intent.extras?.getString("url"))

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout!!.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
            collapsing_toolbar.title = intent.extras?.getString("source")
            collapsing_toolbar.subtitle = intent.extras?.getString("url")
            isShow = true
        } else if(isShow) {
            collapsing_toolbar.title = " "//careful there should a space between double quote otherwise it wont work
            collapsing_toolbar.subtitle = " "//careful there should a space between double quote otherwise it wont work

            isShow = false
        }
    }
}
