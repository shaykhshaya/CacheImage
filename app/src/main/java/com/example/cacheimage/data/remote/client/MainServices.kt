package com.example.cacheimage.data.remote.client

import com.example.cacheimage.data.remote.ImageDto
import com.example.cacheimage.util.Constants
import retrofit2.Response
import retrofit2.http.GET

interface MainServices {

    @GET("${Constants.API_BASE_URL}/content/misc/media-coverages?limit=100")
    suspend fun getRemoteImageList(): Response<List<ImageDto>>

}