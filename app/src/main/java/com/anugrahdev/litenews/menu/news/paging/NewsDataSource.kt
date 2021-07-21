package com.anugrahdev.litenews.menu.news.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.home.models.ErrorModel
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.utils.BusinessException
import com.anugrahdev.litenews.utils.LoadingState
import com.anugrahdev.litenews.utils.jsonToModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NewsDataSource(val scope: CoroutineScope, val repository: NewsRepository, val sources: String, val category: String, val country: String, val msg: (String?) -> Unit) :
    PageKeyedDataSource<Int, Article>() {

    private var unit: MutableList<Article> = mutableListOf()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var state: MutableLiveData<LoadingState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        updateState(LoadingState.LOADING)
        isLoading.postValue(true)
        unit.clear()
        scope.launch {
            repository.getHeadlineNewsPaging2(page = 1, sources = sources, category = category, country = country,  onSuccess = { list, total ->
                updateState(LoadingState.DONE)
                isLoading.postValue(false)
                list?.let { listRecommendation ->
                    unit.addAll(listRecommendation)
                    callback.onResult(listRecommendation, 0, 1)
                }
            }, onError = { ex->
                    updateState(LoadingState.DONE)
                    isLoading.postValue(false)
                    ex.printStackTrace()
                    if (ex is BusinessException) {
                        try {
                            val errorModel = jsonToModel(ErrorModel::class.java, ex.body)
                            msg(errorModel?.message)
                        } catch (e: Exception) {
                            msg(e.localizedMessage)

                        }
                    } else {
                        msg(ex.localizedMessage)
                    }
                })
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Article>
    ) {

    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Article>
    ) {
        updateState(LoadingState.LOADING)
        unit.clear()
        scope.launch {
            repository.getHeadlineNewsPaging2(page = params.key + 1, sources = sources, category = category, country = country,  onSuccess = { list, total ->
                list?.let { listNews ->
                    if (listNews.isEmpty()) {
                        updateState(LoadingState.NO_DATA)
                    } else {
                        updateState(LoadingState.DONE)
                        unit.addAll(listNews)
                        callback.onResult(listNews, params.key + 1)
                    }
                }
            },
                onError = {
                    updateState(LoadingState.DONE)
                })
        }

    }

    private fun updateState(state: LoadingState) {
        this.state.postValue(state)
    }

}