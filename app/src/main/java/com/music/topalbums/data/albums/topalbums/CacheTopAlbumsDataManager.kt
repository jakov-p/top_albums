package com.music.topalbums.data.albums.topalbums

import com.music.topalbums.data.albums.AlbumCollection

class CacheTopAlbumsDataManager(country: String): TopAlbumsDataManager(country)
{
    override suspend fun fetchAlbumCollection(): AlbumCollection
    {
        if(!cache.containsKey(country))
        {
            cache[country] = super.fetchAlbumCollection()
        }
        return cache[country]!!
    }

    companion object
    {
        val cache: MutableMap<String, AlbumCollection> = mutableMapOf()
    }
}