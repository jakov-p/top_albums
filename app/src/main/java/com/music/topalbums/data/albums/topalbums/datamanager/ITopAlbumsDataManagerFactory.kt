package com.music.topalbums.data.albums.topalbums.datamanager


fun interface ITopAlbumsDataManagerFactory
{
    fun create(country: String): ITopAlbumsDataManager
}