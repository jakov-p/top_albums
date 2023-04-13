package com.music.topalbums.data.albums.topalbums.repository

import com.music.topalbums.data.albums.Album

open class FilteredTopAlbumsRepository(country: String, limit :Int): TopAlbumsRepository( country, limit)
{
    private var filteredAlbumCollection: List<Album>? = null

    var filter: ((album: Album)-> Boolean)? = null
        set(value) {
            field = value
            filteredAlbumCollection = null
        }

    override suspend  fun getAlbumsCount(): Int
    {
        if(filteredAlbumCollection == null)
        {
            filteredAlbumCollection = applyFilter()
        }
        return filteredAlbumCollection!!.size
    }

    override suspend fun getAlbums(fromIndex:Int, toIndex: Int): List<Album>
    {
        if(filteredAlbumCollection == null)
        {
            filteredAlbumCollection = applyFilter()
        }

        return (fromIndex..toIndex - 1).map {
            filteredAlbumCollection!![it]
        }
    }


    private suspend fun applyFilter(): List<Album>?
    {
        return  fetchAlbumCollection().list.filter { filter?.invoke(it) ?: true }
    }
}
