package com.music.topalbums.ui.topalbums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.data.albums.TopAlbumsDataManager
import com.music.topalbums.data.albums.TopAlbumsDataManager_with_filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopAlbumsViewModel: ViewModel()
{
    private var currentPagingSource: AlbumsPagingSource? = null
    var country: String  = "us"

    companion object const val PAGE_SIZE = 10
    val topAlbums = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            currentPagingSource = AlbumsPagingSource(TopAlbumsDataManager(country))
            currentPagingSource!!
        })
        .flow.cachedIn(viewModelScope)

    fun applyFilter(filter: TopAlbumsDataManager_with_filter.IFilter)
    {
        TopAlbumsDataManager_with_filter.applyFilter(filter){

        }
    }

    fun startNewLoad(country: String)
    {
        this@TopAlbumsViewModel.country = country
        currentPagingSource?.invalidate()
    }

}