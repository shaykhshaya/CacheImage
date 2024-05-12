package com.example.cacheimage.data.mappers

import com.example.cacheimage.data.local.ImageEntity
import com.example.cacheimage.data.local.ImageLinkEntity
import com.example.cacheimage.data.remote.ImageDto
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.model.ImageLinks

fun ImageDto.toImageEntity(): ImageEntity {
    return ImageEntity(
        basePath = thumbnail.basePath,
        domain = thumbnail.domain,
        key = thumbnail.key
    )
}


fun ImageEntity.toImage(): Image{
    return Image(
        basePath = basePath,
        domain = domain,
        key = key
    )
}

fun ImageLinkEntity.toImageLinks(): ImageLinks{
    return ImageLinks(
        url = url,
        uri = uri
    )
}
