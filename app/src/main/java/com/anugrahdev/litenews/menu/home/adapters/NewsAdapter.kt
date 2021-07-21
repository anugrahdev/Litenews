package com.anugrahdev.litenews.menu.home.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.news.adapters.NewsViewHolder
import com.anugrahdev.litenews.utils.AdapterCallback.diffArticle

class NewsAdapter(val onItemClick:(Article) -> Unit): ListAdapter<Article, NewsViewHolder>(
    diffArticle){


    override fun onCreateViewHolder(parent: ViewGroup, viewTypfe: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
        holder.itemView.setOnClickListener {
            onItemClick(model)
        }
    }

}