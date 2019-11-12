package com.hiker.presentation.mountainObjects

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hiker.R
import com.hiker.data.remote.dto.Image
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(val context: Context, val images: List<Image>) : SliderViewAdapter<SliderAdapterVH>(){

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        val image = images[position]
        viewHolder?.bind(image)
    }

    override fun getCount(): Int {
        return images.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, null)
        return SliderAdapterVH(view)
    }

}