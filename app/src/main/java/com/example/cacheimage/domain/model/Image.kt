package com.example.cacheimage.domain.model


data class Image(
    val basePath: String,
    val domain: String,
    val key: String
){
    val url = "$domain/$basePath/0/$key"
}
