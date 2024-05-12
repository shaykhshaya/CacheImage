package com.example.cacheimage.di

import android.content.Context
import androidx.room.Room
import com.example.cacheimage.CacheImageApplication
import com.example.cacheimage.data.local.ImageDatabase
import com.example.cacheimage.data.local.ImageLinkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context) : CacheImageApplication {
        return context as CacheImageApplication
    }

    @Provides
    @Singleton
    fun provideImageDatabase(@ApplicationContext context: Context): ImageDatabase {
        return Room.databaseBuilder(
            context,
            ImageDatabase::class.java,
            "image.db"
        ).build()
    }


        @Provides
        @Singleton
        fun provideImageLinkDatabase(@ApplicationContext context: Context): ImageLinkDatabase {
            return Room.databaseBuilder(
                context,
                ImageLinkDatabase::class.java,
                "imageLink.db"
            ).build()
        }


}