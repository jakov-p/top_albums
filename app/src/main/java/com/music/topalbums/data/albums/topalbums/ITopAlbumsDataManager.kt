package com.music.topalbums.data.albums.topalbums

import com.music.topalbums.data.albums.Album

interface ITopAlbumsDataManager
{
    val isFullyLoaded: Boolean
    suspend fun getAlbumsCount(isForFullLoad:Boolean): Int
    suspend fun getAlbums(isForFullLoad:Boolean, fromIndex:Int, toIndex: Int): List<Album>
    var filter: ((album: Album) -> Boolean)?
}