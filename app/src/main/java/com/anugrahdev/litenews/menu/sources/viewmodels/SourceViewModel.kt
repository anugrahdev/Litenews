package com.anugrahdev.litenews.menu.sources.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.menu.sources.models.SourceModel
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.utils.Resource
import kotlinx.coroutines.launch

class SourceViewModel(private val repository: NewsRepository, prefs: PreferenceProvider) : ViewModel() {
    val country = prefs.getCountry()

    private val _sources = MutableLiveData<Resource<List<SourceModel>>>()
    val sources: LiveData<Resource<List<SourceModel>>> get() = _sources

    fun getNewsSources(country:String, category:String) = viewModelScope.launch {
        _sources.postValue(Resource.Loading())
        try{
            val response = repository.getNewsSources(category, country)
            _sources.postValue(Resource.Success(response.sources))
        }catch (e: Exception){
            _sources.postValue(Resource.Error(e.message))
        }

    }
}