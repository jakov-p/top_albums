package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.ClientApi
import com.music.topalbums.logger.Logger.loggable

open class SongsRepository(val collectionId: Int)
{
    val TAG = SongsRepository::class.java.simpleName

    protected val clientApi: ClientApi = ClientApi
    protected lateinit var songCollection: SongCollection

    private suspend fun loadSongs(): SongCollection
    {
        loggable.i(TAG, "Fetching songs for an album, collectionId = $collectionId ...")

        val albumSongsCollection = clientApi.getAlbumSongs(collectionId)
        return SongCollection(albumSongsCollection!!)
            .also {
                loggable.i(TAG, "Fetched songs for an album, total track number = ${it.list.size}")
            }
    }

    suspend fun getSongsCount(): Int
    {
        return fetchSongCollection().list.size
    }

    suspend fun getSongs(fromIndex:Int, toIndex: Int): List<Song>
    {
        loggable.i(TAG, "Fetching songs from $fromIndex to $toIndex ...")
        return (fromIndex..toIndex - 1).map {
            loggable.i(TAG, "Fetched song with index = $it ...")
            fetchSongCollection().list[it]
        }.
        also {
            loggable.i(TAG, "Fetched songs, in total =  ${it.size}")
        }
    }

    private suspend fun fetchSongCollection():SongCollection
    {
        if (!::songCollection.isInitialized)
        {
            songCollection = loadSongs()
        }
        return songCollection
    }
}