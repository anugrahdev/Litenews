package com.anugrahdev.litenews.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class NewsDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener,KodeinAware {
    private val prefs: PreferenceProvider by instance<PreferenceProvider>()
    var isShow = true
    var scrollRange: Int = -1
    var liked = false
    private val args:NewsDetailActivityArgs by navArgs()
    private lateinit var viewModel:NewsViewModel
    override val kodein by kodein()
    private val factory: NewsViewModelFactory by instance<NewsViewModelFactory>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        collapsing_toolbar as SubtitleCollapsingToolbarLayout
        appbar.addOnOffsetChangedListener(this)
        val article = args.article

        initData()
        CoroutineScope(Dispatchers.IO).launch {
            if (viewModel.getAnArticle(article.url.toString())){
                liked=true
                fab.setImageDrawable(getDrawable(R.drawable.ic_saved))
            }
        }

        fab.setOnClickListener {
            if(!liked) {
                viewModel.saveArticle(article)
                val anim = AnimationUtils.loadAnimation(
                    fab.getContext(),
                    R.anim.shake
                )
                anim.duration = 200L
                fab.startAnimation(anim)
                fab.setImageDrawable(getDrawable(R.drawable.ic_saved))

                liked=true
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteAnSavedArticle(article.url.toString())
                }
                val anim = AnimationUtils.loadAnimation(
                    fab.getContext(),
                    R.anim.shake
                )
                anim.duration = 200L
                fab.startAnimation(anim)
                fab.setImageDrawable(getDrawable(R.drawable.ic_savebookmark))
                liked=false

            }

        }

    }

    private fun initData(){
        val article = args.article
        Glide.with(this)
            .load(article.urlToImage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(iv_img)




        webView.apply {
            if (prefs.getDarkMode()){
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON)
                }
            }
            val webViewClient: WebViewClient = object: WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressbar.visibility = View.VISIBLE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressbar.visibility = View.GONE
                    super.onPageFinished(view, url)
                }
            }
            loadUrl(article.url)
            webView.webViewClient = webViewClient
            webView.settings.javaScriptEnabled = false
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        }

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val article = args.article

        if (scrollRange == -1) {
            scrollRange = appBarLayout!!.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
            collapsing_toolbar.title = article.source?.name
            collapsing_toolbar.subtitle = article.url
            isShow = true
        } else if(isShow) {
            collapsing_toolbar.title = " "//careful there should a space between double quote otherwise it wont work
            collapsing_toolbar.subtitle = " "//careful there should a space between double quote otherwise it wont work

            isShow = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                val parentIntent = NavUtils.getParentActivityIntent(this)
                parentIntent!!.flags =
                    Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(parentIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
