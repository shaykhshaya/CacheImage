package com.example.cacheimage.data.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageLinkEntity(
    @PrimaryKey
    val  url: String,
    val uri: String?
)
