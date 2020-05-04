package com.anugrahdev.litenews.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Category
import com.anugrahdev.litenews.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val cat: List<Category>
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(
        val itembinding : ItemCategoryBinding
    ): RecyclerView.ViewHolder(itembinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate<ItemCategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_category,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = cat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itembinding.category = cat[position]

        holder.itembinding.imageView.setOnClickListener {
            when (cat[position].name){
                "Sports"->Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_sportsFragment)
            }
        }

    }

}