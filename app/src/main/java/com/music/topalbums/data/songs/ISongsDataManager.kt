package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.ArtistInfo
import com.music.topalbums.clientapi.collection.Song

/**
 * Fetches songs from the internet.
 *
 */
interface ISongsDataManager
{
    /** The total number of songs fetched from the internet
     */
    suspend fun getSongsCount(): Int

    /** Get a range of songs
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return songs in the requested range
     */
    suspend fun getSongs(fromIndex:Int, toIndex: Int): List<Song>

    /**
     * Get artist info
     */
    fun getArtistInfo(): ArtistInfo?
}

