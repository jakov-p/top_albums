package com.music.topalbums.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.TopAlbumsDataManager
import com.music.topalbums.data.albums.TopAlbumsDataManager_with_filter
import com.music.topalbums.data.songs.SongsDataManager

class SongsViewModel: ViewModel()
{
    private val songsDataManager: SongsDataManager = SongsDataManager(album)

    val songs = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            SongsPagingSource(songsDataManager)
        })
        .flow.cachedIn(viewModelScope)

    companion object
    {
        const val PAGE_SIZE = 10
        lateinit var album: Album
    }
}