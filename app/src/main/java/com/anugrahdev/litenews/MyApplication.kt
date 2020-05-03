package com.anugrahdev.litenews

import android.app.Application
import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.repositories.HeadlinesRepository
import com.anugrahdev.litenews.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication:Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))
        bind() from singleton { ApiService() }
        bind() from singleton { HeadlinesRepository(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

    }
}