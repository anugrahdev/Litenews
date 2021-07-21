package com.anugrahdev.litenews.menu.sources.models


import androidx.annotation.Keep

@Keep
data class SourceResponse(
    val sources: List<SourceModel>,
    val status: String
)