package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.collection.ArtistInfo

/**
 * Fetches songs from the internet.
 *
 * Provides partial fetching (paging), although the list was downloaded from the internet as a whole
 *
 * Additionally offers info on artist (only in the json containing songs the artistId can be found, and that is
 * the only reason why the artist info is returned together with songs)
 *
 * @param album the album for which the songs are fetched
 */
open class SongsDataManager(val album: Album): SongsRepository(requireNotNull( album.collectionId)), ISongsDataManager
{
    override fun getArtistInfo(): ArtistInfo?
    {
        return songCollection?.artistInfo
    }
}
