package com.music.topalbums.clientapi.albums.data

import com.music.topalbums.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class TopAlbumsRepository(val country: String) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(onFinished: () -> Unit)
    {
        val limit = 200
        Logger.loggable.i(TAG, "Fetching the top albums list (limit = $limit), country = $country...")
        clientApi.getTopAlbums(country, limit)?.let {
            Logger.loggable.i(TAG, "Fetched top albums list, in total =  ${it.feed?.entry?.size}")
            albumCollection = AlbumCollection(it)
            onFinished.invoke()
        }?:{
            Logger.loggable.w(TAG, "Null returned????")
            throw Exception()
        }


    }
}