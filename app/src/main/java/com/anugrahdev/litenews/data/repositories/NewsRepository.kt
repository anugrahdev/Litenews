package com.anugrahdev.litenews.data.repositories

import com.anugrahdev.litenews.data.db.NewsDatabase
import com.anugrahdev.litenews.data.db.entities.Article
import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.network.SafeApiRequest

class NewsRepository(private val api:ApiService, private val db:NewsDatabase) {

    suspend fun getHeadlinenews(country:String, category:String)=
        api.getTopHeadlines(country, category)

    suspend fun searchNews(searchQuery:String) =
        api.getSearchNews(searchQuery)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteSavedArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getAnArticle(url:String) = db.getArticleDao().getAnArticle(url)

    fun deleteAnArticle(url:String) = db.getArticleDao().deleteAnArticle(url)

}