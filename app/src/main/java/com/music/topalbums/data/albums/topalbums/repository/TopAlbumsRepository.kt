package com.music.topalbums.data.albums.topalbums.repository

import com.music.topalbums.data.albums.AlbumCollection
import com.music.topalbums.data.albums.BasicAlbumsRepository
import com.music.topalbums.logger.Logger

open class TopAlbumsRepository( val country: String, val limit :Int) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(): AlbumCollection
    {
        Logger.loggable.i(TAG, "Fetching the top albums list ($country, $limit), country = $country...")
        val topAlbums = clientApi.getTopAlbums(country, limit)
        if(topAlbums!=null)
        {
            Logger.loggable.i(TAG, "Fetched top albums list ($country, $limit), in total =  ${topAlbums.feed?.entry?.size}")
            return AlbumCollection(topAlbums)
        }
        else
        {
            Logger.loggable.w(TAG, "Null returned????")
            throw Exception()
        }
    }


}