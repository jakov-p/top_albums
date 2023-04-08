package com.music.topalbums.ui.topalbums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.topalbums.ComplexTopAlbumsDataManager
import com.music.topalbums.data.albums.topalbums.ITopAlbumsDataManager

class TopAlbumsViewModel: ViewModel()
{
    private var topAlbumsDataManager: ITopAlbumsDataManager? = null
    private var currentPagingSource: AlbumsPagingSource? = null

    var country: String  = "us"
    var filter: ((album: Album)-> Boolean)? = {
        (it.collectionName?.startsWith("A") ?:false)
        ||
        (it.collectionName?.startsWith("B") ?:false)
                ||
                (it.collectionName?.startsWith("T") ?:false)
                ||
                (it.collectionName?.startsWith("M") ?:false)
                ||
                (it.collectionName?.startsWith("G") ?:false)

    }

    companion object const val PAGE_SIZE = 6
    val topAlbums = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            topAlbumsDataManager =  ComplexTopAlbumsDataManager(country).apply {
                this.filter = this@TopAlbumsViewModel.filter
            }

            currentPagingSource = AlbumsPagingSource(topAlbumsDataManager!!)
            currentPagingSource!!
        })
        .flow.cachedIn(viewModelScope)

    fun applyFilter(filter: (album: Album)-> Boolean )
    {
        topAlbumsDataManager?.filter = filter
    }

    fun startNewLoad(country: String)
    {
        this@TopAlbumsViewModel.country = country
        currentPagingSource?.invalidate()
    }

}