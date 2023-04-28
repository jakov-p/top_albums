package com.music.topalbums.data.albums.artistalbums

import com.music.topalbums.clientapi.collection.Album

/**
 * Provides data for GUI control showing artist albums
 *  */
interface IArtistAlbumsDataManager
{

    /** The total number of albums fetched from the internet
     */
    suspend fun getAlbumsCount(): Int

    /** Get a range of albums fetched from the internet
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return albums in the requested range
     */
    suspend fun getAlbums(fromIndex:Int, toIndex: Int): List<Album>

}

