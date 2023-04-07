package com.music.topalbums.data.albums

import com.music.topalbums.logger.Logger

open class TopAlbumsRepository(val country: String) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(): AlbumCollection
    {
        val limit = 120
        Logger.loggable.i(TAG, "Fetching the top albums list (limit = $limit), country = $country...")
        val topAlbums = clientApi.getTopAlbums(country, limit)
        if(topAlbums!=null)
        {
            Logger.loggable.i(TAG, "Fetched top albums list, in total =  ${topAlbums.feed?.entry?.size}")
            return AlbumCollection(topAlbums)
        }
        else
        {
            Logger.loggable.w(TAG, "Null returned????")
            throw Exception()
        }
    }

}