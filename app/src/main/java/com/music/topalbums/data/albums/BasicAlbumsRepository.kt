package com.music.topalbums.data.albums

import com.music.topalbums.clientapi.ClientApi
import com.music.topalbums.logger.Logger.loggable

/**
 * Basic albums providing class applicable to both TopAlbum download
 * and for ArtistAlbum download.
 */
abstract class BasicAlbumsRepository
{
    val TAG = BasicAlbumsRepository::class.java.simpleName

    protected val clientApi: ClientApi = ClientApi

    //non null value means that download has finished successfully
    protected var albumCollection: AlbumCollection? = null

    /** The concrete implementation of the download request from internet */
    protected suspend abstract fun loadAlbums():AlbumCollection

    /** The total number of albums fetched from the internet
     *
     *  It will initiate the download if it has not started yet.
     *  If already started, it will suspend until finished.
     *  If already loaded, it will immediately  return.
     */
    open suspend  fun getAlbumsCount(): Int
    {
        return fetchAlbumCollection().list.size
    }

    /** Get a range of albums
     *
     *  It will initiate the download if it has not started yet.
     *  If already started, it will suspend until finished.
     *  If already loaded, it will immediately  return.
     *
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return albums in the requested range
     */
    open suspend fun getAlbums(fromIndex:Int, toIndex: Int): List<Album>
    {
        loggable.i(TAG, "Fetching albums from $fromIndex to $toIndex ...")

        return (fromIndex..toIndex - 1).map {
            loggable.i(TAG, "Fetched album with index = $it ...")
            fetchAlbumCollection().list[it]
        }.
        also {
            loggable.i(TAG, "Fetched albums, in total =  ${it.size}")
        }
    }

    /** Lazily fetches albums from internet */
    protected open suspend fun fetchAlbumCollection():AlbumCollection
    {
        if (albumCollection == null)
        {
            albumCollection = loadAlbums()
        }
        return albumCollection!!
    }

}