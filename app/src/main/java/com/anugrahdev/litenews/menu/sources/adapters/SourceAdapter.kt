package com.anugrahdev.litenews.menu.sources.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anugrahdev.litenews.menu.sources.models.SourceModel
import com.anugrahdev.litenews.utils.AdapterCallback.diffSources

class SourceAdapter(val onItemClick:(SourceModel) -> Unit): ListAdapter<SourceModel, SourceViewHolder>(
    diffSources){


    override fun onCreateViewHolder(parent: ViewGroup, viewTypfe: Int): SourceViewHolder {
        return SourceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
        holder.itemView.setOnClickListener {
            onItemClick(model)
        }
    }

}