package com.music.topalbums.data.albums

//TODO To be continued
object TopAlbumsDataManager_with_filter
{
    private lateinit var topAlbumsRepository: FilteredTopAlbumsRepository
    /*
    fun loadAlbums(country: String)
    {
        topAlbumsRepository = FilteredTopAlbumsRepository(country).loadAlbums()
        topAlbumsRepository.loadAlbums({

        })
    }
*/
    fun applyFilter(filter: IFilter, onFilterApplied: () -> Unit)
    {
        topAlbumsRepository.applyFilter(filter)
        onFilterApplied.invoke()
    }

    suspend fun getAlbumsCount(): Int =  topAlbumsRepository.getAlbumsCount()

    suspend fun getAlbums(fromIndex:Int, toIndex: Int): List<Album> = topAlbumsRepository.getAlbums(fromIndex, toIndex)
    private class FilteredTopAlbumsRepository(country: String): TopAlbumsRepository(country)
    {
        private lateinit var originalAlbumCollection: AlbumCollection

        override suspend fun loadAlbums(): AlbumCollection
        {
            return super.loadAlbums().also { it: AlbumCollection ->
                originalAlbumCollection = it
            }
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