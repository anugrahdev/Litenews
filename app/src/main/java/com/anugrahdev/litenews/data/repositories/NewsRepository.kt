package com.anugrahdev.litenews.data.repositories

import com.anugrahdev.litenews.data.db.NewsDatabase
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.home.models.NewsResponse
import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.network.SafeApiRequest

class NewsRepository(private val api:ApiService, private val db:NewsDatabase): SafeApiRequest() {

    suspend fun getHeadlineNews(country:String,
                                category:String,
                                page: Int = 1,
                                onSuccess: (NewsResponse?) -> Unit,
                                onError: (Exception) -> Unit){
        try {
            val result = apiRequest { api.getTopHeadlines(country, category, page) }
            onSuccess(result)
        }catch (e: Exception){
            onError(e)
        }
    }

    suspend fun getHeadlineNewsPaging2(page:Int,
                                       sources: String = "",
                                       country: String = "",
                                       category: String = "",
                                       onSuccess: (MutableList<Article>?, Int?) -> Unit,
                                       onError: (Exception) -> Unit){
        try {
            val result = apiRequest {
                api.getTopHeadlines(
                    page = page, sources = sources, country = country,category = category

                )
            }
            val totalPages: Int = if (result.totalResults > 5){
                (result.totalResults+5-1)/5
            }else {
                1
            }

            onSuccess(result.articles,totalPages)
        }catch (e:Exception){
            onError(e)
        }
    }


    suspend fun searchNews(searchQuery:String)
            = apiRequest { api.getSearchNews(searchQuery) }

    suspend fun getNewsSources(category:String, country: String)
            = apiRequest { api.getSources(category, country) }

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteSavedArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getAnArticle(url:String) = db.getArticleDao().getAnArticle(url)

    fun deleteAnArticle(url:String) = db.getArticleDao().deleteAnArticle(url)

    fun searchSavedArticle(keyword: String) = db.getArticleDao().searchSavedArticle(keyword)

}