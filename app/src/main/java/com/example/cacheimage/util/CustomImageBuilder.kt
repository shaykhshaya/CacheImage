package com.example.cacheimage.util

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.example.cacheimage.data.local.ImageLinkEntity
import com.example.cacheimage.data.mappers.toImageLinks
import com.example.cacheimage.domain.model.ImageLinks
import com.example.cacheimage.domain.repository.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "CustomImageBuilder"

class CustomImageBuilder @Inject constructor(
    private val cacheRepository: ImageRepository,
    private val imageDownloader: ImageDownloader
) {

    private val cache = arrayListOf<ImageLinks>()
    private var builderScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val map = mutableMapOf<String?, String>()

    init {
        getCache()
    }

    private fun getCache() = builderScope.launch(Dispatchers.IO) {
        cacheRepository.getUriList(Dispatchers.IO).collectLatest {
            val dbCache = it.map { it.toImageLinks() }
            cache.clear()
            cache.addAll(dbCache)
        }
    }


    fun load(
        mUrl: String,
        mView: ImageView,
        callback: Callback,
    ) {
        var scope : CoroutineScope? = null

        mView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                Log.d(TAG, "load: mUrl = $mUrl scope = $scope Atached")
                scope = CoroutineScope(Dispatchers.IO)

                if (map[mUrl] != null){
                    setImageViaUri(mView, map[mUrl].toString(), scope!!) {
                        callback.onLoaded()
                    }
                    return
                }

                val cacheItem = cache.find { it.url == mUrl }
                if (cacheItem != null && !cacheItem.uri.isNullOrEmpty()) {
                    map[mUrl] =  cacheItem.uri
                    setImageViaUri(mView, cacheItem.uri, scope!!) {
                        callback.onLoaded()
                    }
                } else {
                    Log.d(TAG, "load: mUrl = $mUrl scope = $scope Atached Downlaoding")
                    imageDownloader.downloadImage(
                        url = mUrl,
                        scope = scope!!,
                        onDownloaded = {
                            map[mUrl] =  it.toString()
                            launch {
                                setImageViaUri(mView, it.toString(), this) {
                                    callback.onLoaded()
                                }

                                withContext(Dispatchers.IO) {
                                    cacheRepository.insertUri(
                                        Dispatchers.IO,
                                        ImageLinkEntity(
                                            mUrl,
                                            it.toString()
                                        ) //Do not use entity here change repo to accept ImageLinks
                                    )
                                }
                            }
                        }
                    )
                }
            }

            override fun onViewDetachedFromWindow(v: View) {
                Log.d(TAG, "load: mUrl = $mUrl  Deteched")
                scope?.cancel()
            }

        })


    }

    private fun setImageViaUri(
        imageView: ImageView,
        uri: String,
        scope: CoroutineScope,
        onLoaded: () -> Unit
    ) =
        scope.launch {
           val bitmap =  getResizeBitmap(
                context = imageView.context,
                uri = uri.toUri(),
                reqHeight = 100,
                reqWidth = 100
            )
            launch(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
                onLoaded()
            }
        }


    inner class with(context: Context) {

        private var url: String = ""
        private lateinit var imageView: ImageView
        private lateinit var callback: Callback

        fun url(url: String) = apply {
            this.url = url
        }

        fun callback(callback: Callback) = apply {
            this.callback = callback
        }

        fun into(view: ImageView) = apply {
            this.imageView = view
        }


        fun build() = load(
            mUrl = url,
            mView = imageView,
            callback = callback
        )
    }

    interface Callback {
        fun onLoaded()
    }
}

