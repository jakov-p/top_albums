package com.music.topalbums.data.albums.topalbums.datamanager

import com.music.topalbums.data.albums.Album

/**
 * Provides data for GUI control showing albums
 *
 * It assumes that a complex algorithm is implemented, where two album lists
 * are combined to offer the GUI control some data as soon as possible.
 * The 'small' list should be fetched significantly faster then the full list,
 * so the user will see some albums even before all of them have been fetched.
 *  */
interface ITopAlbumsDataManager
{
    /** true = the full list has been read, false = the full list has not been read  */
    val isFullyLoaded: Boolean

    /** The total number of albums fetched from the internet
     *  @param isForFullLoad false = read from the 'small' list, true = read from the 'full' list
     */
    suspend fun getAlbumsCount(isForFullLoad:Boolean): Int

    /** Get a range of albums fetched from the internet
     * @param isForFullLoad false = read from the 'small' list, true = read from the 'full' list
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return albums in the requested range
     */
    suspend fun getAlbums(isForFullLoad:Boolean, fromIndex:Int, toIndex: Int): List<Album>

    /** filter to be applied to the read list of albums */
    var filter: ((album: Album) -> Boolean)?
}