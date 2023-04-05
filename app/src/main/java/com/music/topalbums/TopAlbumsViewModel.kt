package com.music.topalbums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.clientapi.albums.data.TopAlbumsDataManager
import com.music.topalbums.pager.AlbumsPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopAlbumsViewModel: ViewModel()
{
    private var currentPagingSource: AlbumsPagingSource? = null
    val isTopListLoaded:LiveData<Boolean> = MutableLiveData(false)

    val pageSize = 8
    val topAlbums = Pager(config = PagingConfig(pageSize = pageSize,initialLoadSize = pageSize ,prefetchDistance = pageSize,
                                    jumpThreshold = pageSize * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            currentPagingSource = AlbumsPagingSource()
            currentPagingSource!!
        })
        .flow.cachedIn(viewModelScope)


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