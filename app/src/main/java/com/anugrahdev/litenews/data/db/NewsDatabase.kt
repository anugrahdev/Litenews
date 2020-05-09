package com.anugrahdev.litenews.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anugrahdev.litenews.data.db.dao.ArticleDao
import com.anugrahdev.litenews.data.db.entities.Article

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var instance:NewsDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context)
        }

        private  fun createDatabase (context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                "news_db.db"
            ).build()
    }

}