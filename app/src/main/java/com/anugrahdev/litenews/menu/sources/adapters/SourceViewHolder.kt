package com.anugrahdev.litenews.menu.sources.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.databinding.ItemSourceBinding
import com.anugrahdev.litenews.menu.sources.models.SourceModel

class SourceViewHolder(val binding: ItemSourceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: SourceModel) {
        binding.tvSource.text = model.name
    }

    companion object {
        fun from(parent: ViewGroup): SourceViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: ItemSourceBinding =
                ItemSourceBinding.inflate(inflater, parent, false)
            return SourceViewHolder(binding)
        }
    }
}