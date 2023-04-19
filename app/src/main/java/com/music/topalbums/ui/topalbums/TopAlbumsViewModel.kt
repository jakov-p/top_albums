package com.music.topalbums.ui.topalbums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import com.music.topalbums.data.albums.topalbums.datamanager.ComplexTopAlbumsDataManager
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManager
import com.music.topalbums.ui.topalbums.filter.FilterTranslator

/**
 * Top albums view model
 *
 * Feeds the GUI with top albums list for a particular country respecting filter and search text criteria
 */
class TopAlbumsViewModel: ViewModel()
{
    //the current country for which top albums are shown
    lateinit var country: String
        private set

    //the current filter applied to the albums list
    var albumFilter: AlbumFilter = AlbumFilter(null, null)
        private set

    //the current search text applied to the albums list
    private var searchText: String? = null

    //provides the filtered list of albums
    private lateinit var topAlbumsDataManager: ITopAlbumsDataManager
    private lateinit var currentPagingSource: AlbumsPagingSource

    companion object const val PAGE_SIZE = 6
    val topAlbums = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            topAlbumsDataManager.apply {
                this.filter = FilterTranslator(albumFilter, searchText)::check
            }

            currentPagingSource = AlbumsPagingSource(topAlbumsDataManager)
            currentPagingSource
        })
        .flow.cachedIn(viewModelScope)

    /** Init the view model */
    fun start( country: String )
    {
        this.country = country
        topAlbumsDataManager =  ComplexTopAlbumsDataManager(country)
    }

    /**
     * The user has created another filter  --> a new album list has to be shown
     * @param albumFilter
     */
    fun applyFilter(albumFilter: AlbumFilter)
    {
        this@TopAlbumsViewModel.albumFilter = albumFilter
        //this is without going to internet to read album list again
        currentPagingSource.invalidate() //forces 'pagingSourceFactory' to create a new stream of albums
    }

    /**
     * The user has changed the search text --> a new album list has to be shown
     * @param searchText
     */
    fun applySearch(searchText: String? )
    {
        this@TopAlbumsViewModel.searchText = searchText
        //this is without going to internet to read album list again
        currentPagingSource.invalidate() //forces 'pagingSourceFactory' to create a new stream of albums
    }

    /**
     * The user has selected a new country --> a new album list has to be shown
     * @param country
     */
    fun startNewLoad(country: String)
    {
        if(country!= this@TopAlbumsViewModel.country)
        {
            this@TopAlbumsViewModel.country = country
            // going to internet to read album list again
            topAlbumsDataManager =  ComplexTopAlbumsDataManager(country)
            currentPagingSource.invalidate() //forces 'pagingSourceFactory' to create a new stream of albums
        }
    }

}