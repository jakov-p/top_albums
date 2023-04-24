package com.music.topalbums.data.albums.artistalbums

import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.data.albums.BasicAlbumsRepository
import com.music.topalbums.logger.Logger

class ArtistAlbumsRepository(val artistId : Int) : BasicAlbumsRepository()
{
    protected override suspend fun loadAlbums(): AlbumCollection
    {
        Logger.loggable.i(TAG, "Fetching albums for an artist, artistID = $artistId ...")

        val songsCollection = clientApi.getArtistAlbums(artistId)
        if(songsCollection!=null)
        {
            Logger.loggable.i(TAG, "Fetched songs for an album, total track number = ${songsCollection.list.size}")
            return songsCollection
        }
        else
        {
            throw Exception()
        }
    }
}