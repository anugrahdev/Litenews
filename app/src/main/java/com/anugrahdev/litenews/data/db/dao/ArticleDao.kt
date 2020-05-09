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

}