package com.anugrahdev.litenews.menu.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.databinding.SliderItemBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    private var mSliderItems: List<Article>
) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {


    inner class SliderAdapterVH(val item : SliderItemBinding): ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return  SliderAdapterVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.slider_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = mSliderItems[position]
        viewHolder.itemView.apply {
            viewHolder.item.data = sliderItem
            setOnClickListener {
                onItemClickListener?.let{ it(sliderItem) }
            }
        }

    }

    override fun getCount(): Int {
        return mSliderItems.size
    }



    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnSliderClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}