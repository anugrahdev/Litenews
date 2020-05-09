package com.anugrahdev.litenews.data.db.entities


import com.anugrahdev.litenews.data.db.entities.Article
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)