package com.anugrahdev.litenews.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.litenews.data.db.entities.Article
import com.anugrahdev.litenews.data.db.entities.NewsResponse
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel(private val repository: NewsRepository, prefs: PreferenceProvider) : ViewModel() {

    val country = prefs.getCountry()

    private val _headlineNews = MutableLiveData<Resource<NewsResponse>>()
    val headlineNews:LiveData<Resource<NewsResponse>> get() = _headlineNews

    private val _sportNews = MutableLiveData<Resource<NewsResponse>>()
    val sportNews:LiveData<Resource<NewsResponse>> get() = _sportNews

    private val _healthNews = MutableLiveData<Resource<NewsResponse>>()
    val healthNews:LiveData<Resource<NewsResponse>> get() = _healthNews

    private val _scienceNews = MutableLiveData<Resource<NewsResponse>>()
    val scienceNews:LiveData<Resource<NewsResponse>> get() = _scienceNews

    private val _entertainmentNews = MutableLiveData<Resource<NewsResponse>>()
    val entertainmentNews:LiveData<Resource<NewsResponse>> get() = _entertainmentNews

    private val _businessNews = MutableLiveData<Resource<NewsResponse>>()
    val businessNews:LiveData<Resource<NewsResponse>> get() = _businessNews

    private val _techNews = MutableLiveData<Resource<NewsResponse>>()
    val techNews:LiveData<Resource<NewsResponse>> get() = _techNews

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    init {
        getHeadlineNews(country," ")
    }

    fun getHeadlineNews(country:String, category:String) = viewModelScope.launch {
        _headlineNews.postValue(Resource.Loading())
        try{
            val response = repository.getHeadlinenews(country, category)
            _headlineNews.postValue(Resource.Success(response))
        }catch (e: IOException){
            _headlineNews.postValue(Resource.Error(e.message))
        }

    }

    fun getTechNews(country:String, category:String) = viewModelScope.launch {
        _techNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _techNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _techNews.postValue(Resource.Error(e.message))
        }
    }

    fun getSportsNews(country:String, category:String) = viewModelScope.launch {
        _sportNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _sportNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _sportNews.postValue(Resource.Error(e.message))
        }
    }

    fun getBusinessNews(country:String, category:String) = viewModelScope.launch {
        _businessNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _businessNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _businessNews.postValue(Resource.Error(e.message))
        }
    }

    fun getScienceNews(country:String, category:String) = viewModelScope.launch {
        _scienceNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _scienceNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _scienceNews.postValue(Resource.Error(e.message))
        }
    }

    fun getHealthNews(country:String, category:String) = viewModelScope.launch {
        _healthNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _healthNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _healthNews.postValue(Resource.Error(e.message))
        }
    }

    fun getEntertainmentNews(country:String, category:String) = viewModelScope.launch {
        _entertainmentNews.postValue(Resource.Loading())
        try {
            val response = repository.getHeadlinenews(country, category)
            _entertainmentNews.postValue(Resource.Success(response))
        } catch (e: IOException) {
            _entertainmentNews.postValue(Resource.Error(e.message))
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
