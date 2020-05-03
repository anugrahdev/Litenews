package com.anugrahdev.litenews.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.headlines.Article
import com.anugrahdev.litenews.databinding.ItemHeadlinenewsBinding

class NewsAdapter(
    private val article: List<Article>
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(
        val itemheadlinesnewsBinding : ItemHeadlinenewsBinding
    ):RecyclerView.ViewHolder(itemheadlinesnewsBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder(
            DataBindingUtil.inflate<ItemHeadlinenewsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_headlinenews,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = article.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.itemheadlinesnewsBinding.headlines = article[position]
    }

}