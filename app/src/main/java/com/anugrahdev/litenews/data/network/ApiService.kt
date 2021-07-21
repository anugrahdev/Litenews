package com.anugrahdev.litenews.data.network

import com.anugrahdev.litenews.MyApplication
import com.anugrahdev.litenews.menu.home.models.NewsResponse
import com.anugrahdev.litenews.menu.sources.models.SourceResponse
import com.anugrahdev.litenews.utils.Constants.Companion.apiKey
import com.anugrahdev.litenews.utils.Constants.Companion.base_url
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country:String,
        @Query("category") category:String = "",
        @Query("page") page:Int = 1,
        @Query("sources") sources:String = "",
        @Query("pageSize") pageSize:Int = 5,
    ):Response<NewsResponse>

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") category:String,
        @Query("country") country:String = "us",
    ):Response<SourceResponse>

    @GET("everything")
    suspend fun getSearchNews(
        @Query("q") query:String,
        @Query("language") language:String="id"

    ):Response<NewsResponse>


    companion object {
        operator fun invoke(connectivityInterceptor: NetworkInterceptor) : ApiService{
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apiKey", apiKey)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val chucker = ChuckerInterceptor.Builder(MyApplication.appContext)
                .alwaysReadResponseBody(true)
                .build()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(requestInterceptor)
                .addInterceptor(chucker)
                .readTimeout(150, TimeUnit.SECONDS)
                .connectTimeout(150, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(base_url)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ApiService::class.java)


        }
    }

}