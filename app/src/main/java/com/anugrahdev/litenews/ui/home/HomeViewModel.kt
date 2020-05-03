package com.anugrahdev.litenews.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anugrahdev.litenews.data.db.entities.headlines.Article
import com.anugrahdev.litenews.data.repositories.HeadlinesRepository
import com.anugrahdev.litenews.utils.Coroutines
import kotlinx.coroutines.Job

class HomeViewModel(private val repository: HeadlinesRepository) : ViewModel() {

    private lateinit var job:Job
    private val _headlines = MutableLiveData<List<Article>>()

    val headlines: LiveData<List<Article>>
        get() = _headlines

    fun getHeadlines(){
        job = Coroutines.ioThenMain(
            { repository.getHeadlines() },
            { _headlines.value = it?.articles }
        )

    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}
