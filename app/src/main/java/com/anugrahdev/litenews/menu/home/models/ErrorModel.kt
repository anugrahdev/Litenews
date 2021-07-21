package com.anugrahdev.litenews.menu.home.models


import androidx.annotation.Keep

@Keep
data class ErrorModel(
    val code: String,
    val message: String,
    val status: String
)