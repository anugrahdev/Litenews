package com.anugrahdev.litenews

import android.app.Application
import android.content.Context
import com.anugrahdev.litenews.data.db.NewsDatabase
import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.network.NetworkInterceptor
import com.anugrahdev.litenews.data.repositories.NewsRepository
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication:Application(), KodeinAware {
    companion object {
        lateinit var appContext: Context
    }
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))
        bind() from singleton { NetworkInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { NewsDatabase(instance()) }
        bind() from singleton { NewsRepository(instance(),instance()) }
        bind() from provider {
            NewsViewModelFactory(
                instance(),instance()
            )
        }

    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}