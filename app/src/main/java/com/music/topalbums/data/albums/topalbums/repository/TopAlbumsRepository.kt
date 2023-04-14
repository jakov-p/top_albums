package com.music.topalbums.data.albums.topalbums.repository

import com.music.topalbums.data.albums.AlbumCollection
import com.music.topalbums.data.albums.BasicAlbumsRepository
import com.music.topalbums.logger.Logger.loggable
/**
 * Downloader of Top albums for a particular country. The maximum  number of the albums
 * to be downloaded can be limited.
 *
 * @param country the short name of the country whose album list is to be downloaded
 *           ('us' for United States, 'fr' for France,...)
 * @param limit  The maximum number of albums to download (it seems that always less than 100
 *           albums are in a country's Top album List on the internet, often below 50 )
 */
open class TopAlbumsRepository( val country: String, val limit :Int) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(): AlbumCollection
    {
        loggable.i(TAG, "Fetching the top albums list ($country, $limit), country = $country...")
        val topAlbums = clientApi.getTopAlbums(country, limit)
        if(topAlbums!=null)
        {
            loggable.i(TAG, "Fetched top albums list ($country, $limit), in total =  ${topAlbums.feed?.topAlbums?.size}")
            return AlbumCollection(topAlbums)
        }
        else
        {
            //this has never happened so far
            loggable.w(TAG, "Null returned ????")
            throw Exception()
        }
    }
}