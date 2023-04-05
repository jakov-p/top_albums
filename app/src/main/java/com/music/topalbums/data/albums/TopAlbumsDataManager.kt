package com.music.topalbums.data.albums

import com.music.topalbums.data.songs.Song

object TopAlbumsDataManager
{
    private lateinit var topAlbumsRepository: FilteredTopAlbumsRepository

    var isLoaded: Boolean = false
    suspend fun loadAlbums(country: String, onLoadAlbumsFinished: () -> Unit)
    {
        isLoaded = false
        topAlbumsRepository = FilteredTopAlbumsRepository(country)
        topAlbumsRepository.loadAlbums({
            isLoaded = true
            onLoadAlbumsFinished()
        })
    }

    fun applyFilter(filter: IFilter, onFilterApplied: () -> Unit)
    {
        topAlbumsRepository.applyFilter(filter)
        onFilterApplied.invoke()
    }

    fun getAlbumsCount(): Int =  topAlbumsRepository.getAlbumsCount()

    fun getAlbums(fromIndex:Int, toIndex: Int): List<Album> = topAlbumsRepository.getAlbums(fromIndex, toIndex)

    suspend fun getSingleAlbumSongs(album: Album): List<Song> = topAlbumsRepository.getSingleAlbumSongs(album)

    private class FilteredTopAlbumsRepository(country: String): TopAlbumsRepository(country)
    {
        private lateinit var originalAlbumCollection: AlbumCollection

        override suspend fun loadAlbums(onLoadAlbumsFinished: () -> Unit)
        {
            super.loadAlbums(onLoadAlbumsFinished)
            originalAlbumCollection = super.albumCollection
        }

        fun applyFilter(filter: IFilter)
        {
            val filteredList = originalAlbumCollection.list.filter { filter.isOk(it) }
            super.albumCollection = AlbumCollection(filteredList)
        }
    }


   interface IFilter
   {
       fun isOk(album: Album):Boolean
   }
}