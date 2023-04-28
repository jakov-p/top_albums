package com.music.topalbums.data.albums.artistalbums

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.logger.Logger

/**
 * Fetches artist's albums from the internet. The albums are sorted by release date
 *
 * Provides partial fetching (paging) , although the list was downloaded from the internet as a whole
 *
 * @param artistId the ID of the artist for which the songs are fetched
 */
open class ArtistAlbumsDataManager(artistId : Int): ArtistAlbumsRepository(artistId), IArtistAlbumsDataManager
{
    override suspend fun loadAlbums(): AlbumCollection
    {
        val albums = super.loadAlbums()
        val sortedList: List<Album> = albums.list.sortedByDescending { album: Album -> album.releaseDate }
        return AlbumCollection(sortedList)
    }
}
