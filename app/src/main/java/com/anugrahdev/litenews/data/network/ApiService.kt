package com.anugrahdev.litenews.data.network

import com.anugrahdev.litenews.data.db.entities.NewsResponse
import com.anugrahdev.litenews.utils.Constants.Companion.apiKey
import com.anugrahdev.litenews.utils.Constants.Companion.base_url
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country:String,
        @Query("category") category:String = " ",
        @Query("pageSize") pageSize:Int = 100
    ):Response<NewsResponse>

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

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(requestInterceptor)
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)


        }
    }

}