package com.music.topalbums.ui.topalbums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import com.music.topalbums.data.albums.topalbums.ComplexTopAlbumsDataManager
import com.music.topalbums.data.albums.topalbums.ITopAlbumsDataManager
import com.music.topalbums.ui.topalbums.filter.FilterTranslator

class TopAlbumsViewModel: ViewModel()
{
    var country: String  = "us"
        private set

    var albumFilter: AlbumFilter = AlbumFilter(null, null)
        private set

    private var searchText: String? = null

    private var topAlbumsDataManager: ITopAlbumsDataManager =  ComplexTopAlbumsDataManager(country)
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

    fun applyFilter(albumFilter: AlbumFilter)
    {
        this@TopAlbumsViewModel.albumFilter = albumFilter
        currentPagingSource.invalidate()
    }

    fun applySearch(searchText: String? )
    {
        this@TopAlbumsViewModel.searchText = searchText
        currentPagingSource.invalidate()
    }

    fun startNewLoad(country: String)
    {
        if(country!= this@TopAlbumsViewModel.country)
        {
            this@TopAlbumsViewModel.country = country
            topAlbumsDataManager =  ComplexTopAlbumsDataManager(country)
            currentPagingSource.invalidate()
        }
    }

}