package com.music.topalbums.di

import com.music.topalbums.clientapi.retrofit.RetrofitClient
import com.music.topalbums.clientapi.retrofit.ServerUrl
import com.music.topalbums.data.albums.artistalbums.ArtistAlbumsDataManager
import com.music.topalbums.data.albums.artistalbums.IArtistAlbumsDataManagerFactory
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
    fun createArtistAlbumsDataManagerFactory(): IArtistAlbumsDataManagerFactory
    {
        return IArtistAlbumsDataManagerFactory { ArtistAlbumsDataManager(it) }
    }


    @Singleton
    @Provides
    fun createSongsDataManagerFactory(): ISongsDataManagerFactory
    {
        return ISongsDataManagerFactory { SongsDataManager(it) }
    }


    @ServerUrl
    @Provides
    @Singleton
    fun getServerUrl():String = RetrofitClient.SERVER_URL

}