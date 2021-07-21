package com.anugrahdev.litenews.utils

import androidx.recyclerview.widget.DiffUtil
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.sources.models.SourceModel

object AdapterCallback {
    val diffSources = object : DiffUtil.ItemCallback<SourceModel>() {
        override fun areItemsTheSame(
            oldItem: SourceModel,
            newItem: SourceModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SourceModel,
            newItem: SourceModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val diffArticle = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }
    }
}