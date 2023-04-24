package com.music.topalbums.di

import com.music.topalbums.data.albums.topalbums.datamanager.ComplexTopAlbumsDataManager
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManagerFactory
import com.music.topalbums.data.songs.ISongsDataManagerFactory
import com.music.topalbums.data.songs.SongsDataManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule
{
    @Singleton
    @Provides
    fun createTopAlbumsDataManagerFactory(): ITopAlbumsDataManagerFactory
    {
        return ITopAlbumsDataManagerFactory { ComplexTopAlbumsDataManager(it) }
    }


    @Singleton
    @Provides
    fun createSongsDataManagerFactory(): ISongsDataManagerFactory
    {
        return ISongsDataManagerFactory { SongsDataManager(it) }
    }

}