package com.hiker.presentation.mountainObjects

import android.view.View
import android.widget.ImageView
import com.hiker.R
import com.hiker.data.remote.dto.Image
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso

class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
    private val imageViewBackground : ImageView = itemView.findViewById(R.id.image_item_imageView)

    fun bind(image: Image){
        Picasso.get()
            .load(image.url)
            .fit()
            .centerCrop()
            .into(imageViewBackground)
    }
}