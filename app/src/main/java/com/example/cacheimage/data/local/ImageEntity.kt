package com.example.cacheimage.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageEntity(
    @PrimaryKey
    val basePath: String,
    val domain: String,
    val key: String
)


