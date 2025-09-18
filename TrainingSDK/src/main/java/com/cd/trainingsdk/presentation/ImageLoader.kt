package com.cd.trainingsdk.presentation

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.cd.trainingsdk.presentation.ui.beans.ImageLoadRequest

internal object ImageLoader {

    private var imageLoader: ImageLoader? = null

    fun getImageLoader(context: Context): ImageLoader {
        return imageLoader ?: ImageLoader(context)
    }

    fun loadImages(context: Context, imagesList: List<ImageLoadRequest>) {
        imagesList.forEach {
            val request = ImageRequest.Builder(context)
                .data(it.url)
                .size(it.width.toInt(), it.height.toInt())
                .tag(it.url)
                .build()
            getImageLoader(context).enqueue(request)
        }
    }


    fun clear() {
        imageLoader = null
    }
}