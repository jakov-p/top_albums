package com.music.topalbums.data.albums.artistalbums

import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.data.albums.BasicAlbumsRepository
import com.music.topalbums.logger.Logger

/**
 * Fetches artist albums from the internet.
 *
 * Note that it is not possible to read only a part of the list from the internet
 * (e.g from 12th to 15th song in the list). So it is not possible to have 'paging' directly at the data source - to
 * download from the internet the exact page that is about to be shown in GUI.
 * In other words, it is possible only to download the full album list.
 *
 * But it is not a performance problem, because the download finishes quickly.
 */
open class ArtistAlbumsRepository(val artistId : Int) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(): AlbumCollection
    {
        Logger.loggable.i(TAG, "Fetching albums for an artist, artistID = $artistId ...")

        val albumCollection = clientApi.getArtistAlbums(artistId)
        if(albumCollection!=null)
        {
            Logger.loggable.i(TAG, "Fetched albums for an artist, total album number = ${albumCollection.list.size}")
            return albumCollection
        }
        else
        {
            throw Exception()
        }
    }
}