package com.music.topalbums.ui.artistalbums.helpers

import android.os.Bundle
import com.music.topalbums.clientapi.collection.ArtistInfo

/**
 * Puts artist data into and extracts from a bundle.
 * Helps with the transfer of the artist parameters from one fragment to another.
 */
object ParamsHandler
{
    const val PARAM_ARTIST_INFO = "artist_info"

    fun getArtistInfo(bundle: Bundle?) : ArtistInfo? = bundle?.getParcelable(PARAM_ARTIST_INFO ) as ArtistInfo?

    fun createBundle(artistInfo: ArtistInfo): Bundle = Bundle().apply {
        putParcelable(PARAM_ARTIST_INFO, artistInfo)
    }
}