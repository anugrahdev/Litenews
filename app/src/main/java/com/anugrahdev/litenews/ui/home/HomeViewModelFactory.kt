package com.anugrahdev.litenews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.litenews.data.repositories.HeadlinesRepository

class HomeViewModelFactory(private val repository: HeadlinesRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }

}