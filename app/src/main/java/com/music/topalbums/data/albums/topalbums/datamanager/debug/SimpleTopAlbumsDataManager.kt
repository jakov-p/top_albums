package com.music.topalbums.data.albums.topalbums.datamanager.debug

import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManager
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository

/**
 * Simple top albums data manager for debugging.
 * It just downloads the full album list without any complications with an additional 'small' album list.
 *
 * @param country the short name of the country whose album list is to be downloaded
 *           ('us' for United States, 'fr' for France,...)
 */
open class SimpleTopAlbumsDataManager(country: String): FilteredTopAlbumsRepository(country, 100), ITopAlbumsDataManager
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