package com.anugrahdev.litenews.menu.sources.models


import androidx.annotation.Keep

@Keep
data class SourceModel(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)