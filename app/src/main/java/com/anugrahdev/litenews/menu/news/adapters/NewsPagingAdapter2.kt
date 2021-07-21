package com.anugrahdev.litenews.menu.news.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.utils.AdapterCallback
import com.anugrahdev.litenews.utils.LoadingState
import com.anugrahdev.litenews.utils.widgets.LoadMoreViewHolder

class NewsPagingAdapter2 : PagedListAdapter<Article, RecyclerView.ViewHolder>(
    AdapterCallback.diffArticle
) {

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_LOAD = 2
    }

    private var loadingState = LoadingState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return NewsViewHolder.from(parent)
        } else {
            return LoadMoreViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            getItem(position)?.let { model ->
                holder.bind(model)
                holder.itemView.setOnClickListener {

                    onItemClickListener?.let { it(model) }

                }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) VIEW_TYPE_ITEM else VIEW_TYPE_LOAD
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && loadingState == LoadingState.LOADING
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    fun noMoreData() {
        loadingState = LoadingState.DONE
        notifyDataSetChanged()
    }

}