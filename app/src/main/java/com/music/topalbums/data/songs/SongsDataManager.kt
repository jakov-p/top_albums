package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.collection.ArtistInfo

/**
 * Fetches songs from the internet.
 *
 * Provides partial fetching (paging) , although the list was downloaded from the internet as a whole
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
