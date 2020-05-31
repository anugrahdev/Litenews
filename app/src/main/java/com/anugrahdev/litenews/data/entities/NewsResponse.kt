package com.anugrahdev.litenews.data.entities


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)