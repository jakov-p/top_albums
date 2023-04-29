package com.music.topalbums.ui.songs.helpers

import android.os.Bundle
import com.music.topalbums.clientapi.collection.Album

/**
 * Puts album object into and extracts from a bundle. Also provides information from which
 * fragment we have come from.
 * Helps with the transfer of the album parameter from one fragment to another.
 */
object ParamsHandler
{
    const val PARAM_ALBUM = "album"
    const val PARAM_IS_FROM_TOP = "is_from_top"

    /** album for which the songs are to be shown */
    fun getAlbum(bundle: Bundle?) : Album? = bundle?.getParcelable(PARAM_ALBUM) as Album?

    /** have we come here from 'TopAlbumsFragment' */
    fun isFromTopAlbums(bundle: Bundle?) : Boolean? = bundle?.getBoolean(PARAM_IS_FROM_TOP)

    fun createBundle(album: Album, isFromTopAlbums: Boolean): Bundle = Bundle().apply {
        putParcelable(PARAM_ALBUM, album)
        putBoolean(PARAM_IS_FROM_TOP, isFromTopAlbums)
    }
}