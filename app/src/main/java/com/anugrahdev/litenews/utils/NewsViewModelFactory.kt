package com.anugrahdev.litenews.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.menu.news.viewmodels.NewsArticleViewModel
import com.anugrahdev.litenews.menu.sources.viewmodels.SourceViewModel
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.menu.about.viewmodels.NewsViewModel

class NewsViewModelFactory(private val repository: NewsRepository, private val prefs:PreferenceProvider):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == NewsViewModel::class.java) {
            return NewsViewModel(repository, prefs) as T
        }else if (modelClass == SourceViewModel::class.java) {
            return SourceViewModel(repository, prefs) as T
        }else if (modelClass == NewsArticleViewModel::class.java) {
            return NewsArticleViewModel(repository, prefs) as T
        }
        return super.create(modelClass)
    }

}