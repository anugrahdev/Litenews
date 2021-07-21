package com.anugrahdev.litenews.menu.about.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.home.models.ErrorModel
import com.anugrahdev.litenews.menu.home.models.NewsResponse
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.utils.BusinessException
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.jsonToModel
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel(private val repository: NewsRepository, prefs: PreferenceProvider) : ViewModel() {

    val country = prefs.getCountry()

    private val _headlineNews = MutableLiveData<Resource<NewsResponse>>()
    val headlineNews:LiveData<Resource<NewsResponse>> get() = _headlineNews

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var msg: MutableLiveData<String> = MutableLiveData()

    fun getHeadlineNews(country:String, category:String) = viewModelScope.launch {
        _headlineNews.postValue(Resource.Loading())
        try{
            repository.getHeadlineNews(country, category, onSuccess = {
                _headlineNews.postValue(Resource.Success(it))
            }, onError = {ex->
                ex.printStackTrace()
                if (ex is BusinessException) {
                    try {
                        val errorModel = jsonToModel(ErrorModel::class.java, ex.body)
                        msg.postValue(errorModel?.message)
                    } catch (e: Exception) {
                        msg.postValue(e.localizedMessage)

                    }
                } else {
                    msg.postValue(ex.localizedMessage)
                }
            })

        }catch (e: IOException){
            _headlineNews.postValue(Resource.Error(e.message))
        }

    }


    fun searchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery)
        searchNews.postValue(Resource.Success(response))
    }


    fun saveArticle(article: Article) = viewModelScope.launch{
        repository.upsert(article)
    }

    fun getSavedArticle() = repository.getSavedNews()

    fun deleteSavedArticle(article: Article) = viewModelScope.launch {
        repository.deleteSavedArticle(article)
    }

    fun getAnArticle(url:String) = repository.getAnArticle(url)

    fun deleteAnSavedArticle(url: String) = repository.deleteAnArticle(url)

    fun searchSavedArticle(keyword: String) = repository.searchSavedArticle(keyword)
}
