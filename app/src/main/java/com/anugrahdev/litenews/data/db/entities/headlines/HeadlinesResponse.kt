package com.anugrahdev.litenews.data.db.entities.headlines


import com.google.gson.annotations.SerializedName

data class HeadlinesResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)