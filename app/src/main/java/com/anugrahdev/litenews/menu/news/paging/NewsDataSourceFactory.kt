package com.anugrahdev.litenews.menu.news.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.data.repositories.NewsRepository
import kotlinx.coroutines.CoroutineScope

class NewsDataSourceFactory(val scope: CoroutineScope, val repository: NewsRepository, val sources: String, val category: String, val country: String, val msg: (String?) -> Unit) :
    DataSource.Factory<Int, Article>() {

    val newsDataSource: MutableLiveData<NewsDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, Article> {
        val source = NewsDataSource(scope = scope, repository = repository, sources = sources, category = category, country = country) {
            msg(it)
        }
        newsDataSource.postValue(source)
        return source
    }

}