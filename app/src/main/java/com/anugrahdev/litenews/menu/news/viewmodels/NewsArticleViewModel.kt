package com.anugrahdev.litenews.menu.news.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.paging.*
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.menu.news.paging.NewsDataSource
import com.anugrahdev.litenews.menu.news.paging.NewsDataSourceFactory
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.utils.LoadingState

class NewsArticleViewModel(private val repository: NewsRepository, private val prefs: PreferenceProvider) : ViewModel() {

    var source:String = ""
    var category: String = ""
    var country = ""

    var msg: MutableLiveData<String> = MutableLiveData()
    private lateinit var unitDocumentDataSourceFactory: NewsDataSourceFactory

    lateinit var listData: LiveData<PagedList<Article>>
    var isEmpty: ObservableBoolean = ObservableBoolean(false)

    fun init(){
        if (category != ""){
            country = prefs.getCountry()
        }
       unitDocumentDataSourceFactory = NewsDataSourceFactory(viewModelScope, repository, source, category, country) {
            it?.let { text ->
                msg.postValue(text)
            }
        }
        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setPrefetchDistance(1)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()
        listData = LivePagedListBuilder(unitDocumentDataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Article>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    isEmpty.set(true)
                }
            })
            .build()
    }

    fun isLoading(): LiveData<Boolean> {
        return Transformations.switchMap(
            unitDocumentDataSourceFactory.newsDataSource,
            NewsDataSource::isLoading
        )
    }

    fun getLoadingState(): LiveData<LoadingState> {
        return Transformations.switchMap(
            unitDocumentDataSourceFactory.newsDataSource,
            NewsDataSource::state
        )
    }
}