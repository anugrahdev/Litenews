package com.anugrahdev.litenews.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anugrahdev.litenews.data.db.entities.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM Article")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM Article WHERE url = :urlparams")
    fun getAnArticle(urlparams:String):Boolean

    @Query("DELETE FROM Article WHERE url = :urlparams")
    fun deleteAnArticle(urlparams: String)
}