package com.anugrahdev.litenews.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Article
import com.anugrahdev.litenews.databinding.ItemNewsBinding

class News_Adapter : RecyclerView.Adapter<News_Adapter.ArticleViewHolder>(){

    inner class ArticleViewHolder(val item : ItemNewsBinding):RecyclerView.ViewHolder(item.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,  differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return  ArticleViewHolder(
            DataBindingUtil.inflate<ItemNewsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_news,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            holder.item.news = article
            setOnClickListener {
                onItemClickListener?.let{ it(article) }
            }
        }



    }

    private var onItemClickListener: ((Article)->Unit)? = null

    fun setOnItemClickListener(listener : (Article)->Unit){
        onItemClickListener = listener
    }
}