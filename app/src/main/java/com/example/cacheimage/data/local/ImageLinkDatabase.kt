package com.example.cacheimage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cacheimage.domain.model.ImageLinks

@Database(entities = [ImageLinkEntity::class], version = 1)
abstract class ImageLinkDatabase: RoomDatabase() {

    abstract val dao: ImageLinkDao

}