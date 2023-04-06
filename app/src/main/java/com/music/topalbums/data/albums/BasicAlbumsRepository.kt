package com.music.topalbums.data.albums

import com.music.topalbums.clientapi.ClientApi
import com.music.topalbums.data.songs.Song
import com.music.topalbums.data.songs.SongCollection
import com.music.topalbums.logger.Logger.loggable

abstract class BasicAlbumsRepository
{
    val TAG = BasicAlbumsRepository::class.java.simpleName

    protected val clientApi: ClientApi = ClientApi
    protected  lateinit var albumCollection: AlbumCollection

    suspend abstract fun loadAlbums(onFinished: () -> Unit)

    fun getAlbumsCount(): Int
    {
        return albumCollection.list.size
    }

    fun getAlbums(fromIndex:Int, toIndex: Int): List<Album>
    {
        loggable.i(TAG, "Fetching albums from $fromIndex to $toIndex ...")

        return (fromIndex..toIndex - 1).map {
            loggable.i(TAG, "Fetched album with index = $it ...")
            albumCollection.list[it]
        }.
        also {
            loggable.i(TAG, "Fetched albums, in total =  ${it.size}")
        }
    }

}