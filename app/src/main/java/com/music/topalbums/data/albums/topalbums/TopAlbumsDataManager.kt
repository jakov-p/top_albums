package com.music.topalbums.data.albums.topalbums

import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository

open class TopAlbumsDataManager(country: String): FilteredTopAlbumsRepository(country, 100), ITopAlbumsDataManager
{
    override val isFullyLoaded: Boolean = true

    override suspend fun getAlbumsCount(isForFullLoad: Boolean): Int
    {
        return getAlbumsCount()
    }

    override suspend fun getAlbums(isForFullLoad: Boolean, fromIndex: Int, toIndex: Int): List<Album>
    {
        return getAlbums(fromIndex, toIndex)
    }
}