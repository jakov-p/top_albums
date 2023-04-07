package com.music.topalbums.data.albums

import com.music.topalbums.data.songs.SongCollection
import com.music.topalbums.logger.Logger

class ArtistAlbumsRepository(val artistId : Int) : BasicAlbumsRepository()
{
    protected override suspend fun loadAlbums(): AlbumCollection
    {
        Logger.loggable.i(TAG, "Fetching albums for an artist, artistID = $artistId ...")

        val albumSongsCollection = clientApi.getArtistAlbums(artistId)
        if(albumSongsCollection!=null)
        {
            Logger.loggable.i(TAG, "Fetched songs for an album, total track number = ${albumSongsCollection.resultCount}")
            return AlbumCollection(albumSongsCollection)
        }
        else
        {
            throw Exception()
        }
    }
}