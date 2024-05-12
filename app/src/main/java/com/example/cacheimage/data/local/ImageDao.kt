package com.example.cacheimage.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Upsert
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("SELECT * FROM imageentity")
    fun getAll():Flow<List<ImageEntity>>


}