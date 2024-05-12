package com.example.cacheimage.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.cacheimage.data.local.ImageEntity
import com.example.cacheimage.data.local.ImageLinkEntity
import com.example.cacheimage.data.remote.ImageDto
import com.example.cacheimage.domain.model.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ImageRepository {

   /* suspend fun fetchImageListAndSaveToDatabase(dispatcher: CoroutineDispatcher)


    suspend fun getImageListLocally(dispatcher: CoroutineDispatcher): Flow<List<ImageEntity>>*/

    suspend fun downloadImageAndGetUri(dispatcher: CoroutineDispatcher, context: Context, imageUrl: String): Uri?

    suspend fun getList(dispatcher: CoroutineDispatcher): Flow<List<Image>>

    suspend fun insertUri(dispatcher: CoroutineDispatcher, imageLinkEntity: ImageLinkEntity)

    suspend fun getUriList(dispatcher: CoroutineDispatcher): Flow<List<ImageLinkEntity>>


}