package com.music.topalbums.ui.topalbums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.data.albums.TopAlbumsDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopAlbumsViewModel: ViewModel()
{
    private var currentPagingSource: AlbumsPagingSource? = null
    val isTopListLoaded:LiveData<Boolean> = MutableLiveData(false)

    companion object const val PAGE_SIZE = 10
    val topAlbums = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            currentPagingSource = AlbumsPagingSource()
            currentPagingSource!!
        })
        .flow.cachedIn(viewModelScope)


    init
    {
        println("usao")
    }
    fun applyFilter(filter: TopAlbumsDataManager.IFilter)
    {
        TopAlbumsDataManager.applyFilter(filter){

        }
    }

    fun start(country: String)
    {
        this.viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                (isTopListLoaded as MutableLiveData).postValue( false)
                currentPagingSource?.invalidate()
                TopAlbumsDataManager.loadAlbums(country) {
                    (isTopListLoaded as MutableLiveData).postValue( true)
                }
            }
        }
    }

}