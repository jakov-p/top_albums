package com.music.topalbums.data.albums.topalbums.repository

import com.music.topalbums.data.albums.Album
import com.music.topalbums.logger.Logger.loggable

/**
 * This repository implements filtering functionality over the basic top album list downloader.
 * A filter can be dinamically assigned, and it will be applied to the list fetched by the
 * basic top album list downloader.
 *
 * @param country the short name of the country whose album list is to be downloaded
 *           ('us' for United States, 'fr' for France,...)
 * @param limit  The maximum number of albums to download (it seems that always less than 100
 *           albums are in a country's Top album List on the internet, often below 50 )
 */
open class FilteredTopAlbumsRepository(country: String, limit :Int): TopAlbumsRepository( country, limit)
{
    //the list filtered by the current active filter
    //(null value means that a new filter still has to be applied)
    private var filteredAlbumCollection: List<Album>? = null

    var filter: ((album: Album)-> Boolean)? = null
        set(value) {
            loggable.i(TAG, "A new filter to be applied.")

            //the 'filteredAlbumCollection' is nullified to communicate that a new filter has to be applied
            field = value
            filteredAlbumCollection = null
        }

    override suspend  fun getAlbumsCount(): Int
    {
        if(filteredAlbumCollection == null)   //still the new filter has not been applied
        {
            filteredAlbumCollection = applyFilter()
        }

        //here we are sure that the 'filteredAlbumCollection' is ready for usage
        return filteredAlbumCollection!!.size
    }

    override suspend fun getAlbums(fromIndex:Int, toIndex: Int): List<Album>
    {
        if(filteredAlbumCollection == null)    //still the new filter has not been applied
        {
            filteredAlbumCollection = applyFilter()
        }

        //here we are sure that the 'filteredAlbumCollection' is ready for usage
        return (fromIndex..toIndex - 1).map {
            filteredAlbumCollection!![it]
        }
    }

    /**
     * Go through the albums list and check each album if they pass the filter.
     * Returns the list of albums passing the filter check.
     */
    private suspend fun applyFilter(): List<Album>?
    {
        //if filter = null, then each album passes the check
        return  fetchAlbumCollection().list.filter { filter?.invoke(it) ?: true }
    }
}
