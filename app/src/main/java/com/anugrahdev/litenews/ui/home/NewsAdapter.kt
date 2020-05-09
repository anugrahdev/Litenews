package com.anugrahdev.litenews.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Article
import com.anugrahdev.litenews.databinding.ItemNewsBinding
import com.anugrahdev.litenews.ui.NewsDetailActivity

class NewsAdapter(
    private val article: List<Article>, private val
    context: Context
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(
        val item : ItemNewsBinding
    ):RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder(
            DataBindingUtil.inflate<ItemNewsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_news,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
//        showShimmer?SHIMMER_ITEM_NUMBER :
        return  article.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.item.news = article[position]
        holder.item.root.setOnClickListener {
            Intent(context, NewsDetailActivity::class.java).also { intent->
                intent.putExtra("url", article[position].url);
                intent.putExtra("img", article[position].urlToImage);
                intent.putExtra("source", article[position].source?.name);
                intent.putExtra("author",article[position].author);
                intent.putExtra("title",article[position].title);
                intent.putExtra("date",article[position].publishedAt)


                context.startActivity(intent)
            }
        }

    }

}