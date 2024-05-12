package com.example.cacheimage.di

import com.example.cacheimage.data.local.ImageDatabase
import com.example.cacheimage.data.local.ImageLinkDatabase
import com.example.cacheimage.data.remote.client.MainServices
import com.example.cacheimage.domain.repository.ImageRepository
import com.example.cacheimage.data.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideImageRepository(
        mainServices: MainServices,
        imageDb: ImageDatabase,
        imageLinkDb: ImageLinkDatabase
    ): ImageRepository {
        return ImageRepositoryImpl(mainServices, imageDb, imageLinkDb)
    }

}
