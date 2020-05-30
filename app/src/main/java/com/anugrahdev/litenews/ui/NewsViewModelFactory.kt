package com.anugrahdev.litenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.preferences.PreferenceProvider

class NewsViewModelFactory(private val repository: NewsRepository, private val prefs:PreferenceProvider):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository, prefs) as T
    }

}