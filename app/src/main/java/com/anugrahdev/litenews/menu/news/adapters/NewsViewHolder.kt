package com.anugrahdev.litenews.menu.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.databinding.ItemNewsBinding

class NewsViewHolder(val binding: ItemNewsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Article) {
        binding.news = model
    }

    companion object {
        fun from(parent: ViewGroup): NewsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: ItemNewsBinding =
                ItemNewsBinding.inflate(inflater, parent, false)
            return NewsViewHolder(binding)
        }
    }
}