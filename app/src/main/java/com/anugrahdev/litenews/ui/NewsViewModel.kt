package com.anugrahdev.litenews.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.litenews.data.db.entities.Article
import com.anugrahdev.litenews.data.db.entities.NewsResponse
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {


    val news : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
//    init {
//        getNews("id"," ")
//    }

    fun getNews(country:String, category:String) = viewModelScope.launch {
        news.postValue(Resource.Loading())
        val response = repository.getHeadlinenews(country, category)
        news.postValue(handleNewsResponse(response))
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery)
        searchNews.postValue(handleSearchNewsResponse(response))
    }



    private fun handleNewsResponse(response : Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response : Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
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
}
