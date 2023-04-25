package com.music.topalbums.data.albums.topalbums.datamanager.debug

import com.music.topalbums.clientapi.collection.AlbumCollection

/**
 * Simple top albums data manager for debugging.
 * It just downloads the full album list without any complications with an additional 'small' album list.

 * But also caches old results so if the top album list of a country has already been downloaded in the past,
 * then it will be read from the cache. This saves time, but fills the memory with old downloads.
 *
 * @param country the short name of the country whose album list is to be downloaded
 *           ('us' for United States, 'fr' for France,...)
 */
class CacheTopAlbumsDataManager(country: String): SimpleTopAlbumsDataManager(country)
{
    override suspend fun fetchAlbumCollection(): AlbumCollection
    {
        if(!cache.containsKey(country))
        {
            cache[country] = super.fetchAlbumCollection()
        }
        return requireNotNull(cache[country])
    }

    companion object
    {
        val cache: MutableMap<String, AlbumCollection> = mutableMapOf()
    }
}