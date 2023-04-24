package com.music.topalbums.data.songs

import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.collection.Song
import com.music.topalbums.clientapi.collection.SongCollection
import com.music.topalbums.logger.Logger.loggable
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Fetches songs from the internet.
 *
 * Note that it is not possible to read only a part of the list from the internet
 * (e.g from 12th to 15th song in the list). So it is not possible to have 'paging' directly at the data source - to
 * download from the internet the exact page that is about to be shown in GUI.
 * In other words, it is possible only to download the full song list.
 *
 * But it is not a performance problem, because the download finishes quickly.
 */
open class SongsRepository(val collectionId: Int)
{
    val TAG = SongsRepository::class.java.simpleName

    protected val  clientApi: IClientApi = EntryPoints.get(TopAlbumsApp.appContext, ISongsRepositoryEntryPoint::class.java).getClientApi()

    //non null value means that download has finished successfully
    protected var songCollection: SongCollection? = null

    /** The concrete implementation of the download request from internet */
    private suspend fun loadSongs(): SongCollection
    {
        loggable.i(TAG, "Fetching songs for an album, collectionId = $collectionId ...")

        val songsCollection = clientApi.getAlbumSongs(collectionId)
        return songsCollection!!
            .also {
                loggable.i(TAG, "Fetched songs for an album, total track number = ${it.list.size}")
            }
    }

    /** The total number of songs fetched from the internet
     *
     *  It will initiate the download if it has not started yet.
     *  If already started, it will suspend until finished.
     *  If already loaded, it will immediately  return.
     */
    suspend fun getSongsCount(): Int
    {
        return fetchSongCollection().list.size
    }

    /** Get a range of songs
     *
     *  It will initiate the download if it has not started yet.
     *  If already started, it will suspend until finished.
     *  If already loaded, it will immediately  return.
     *
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return songs in the requested range
     */
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

    /** Lazily fetches songs from internet */
    protected suspend fun fetchSongCollection(): SongCollection
    {
        if (songCollection == null)
        {
            songCollection = loadSongs()
        }
        return songCollection!!
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ISongsRepositoryEntryPoint
    {
        fun getClientApi(): IClientApi
    }
}