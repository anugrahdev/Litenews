package com.anugrahdev.litenews.ui.home

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Category
import com.anugrahdev.litenews.databinding.ItemCategoryBinding
import com.xwray.groupie.databinding.BindableItem

class CategoryItem(private val category: Category)  : BindableItem<ItemCategoryBinding>() {
    override fun getLayout(): Int = R.layout.item_category

    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        viewBinding.category = category
    }

}