package com.example.cacheimage.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.cacheimage.domain.model.ImageLinks
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageLinkDao {

    @Upsert
    suspend fun insertImagesLink(images: ImageLinkEntity)

    @Query("SELECT * FROM imagelinkentity")
    fun getAllImagesLink():Flow<List<ImageLinkEntity>>


}