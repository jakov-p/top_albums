package com.music.topalbums.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.songs.ISongsDataManagerFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

/**
 * Songs view model
 *
 * Feeds the GUI with the song list for a particular album
 */
class SongsViewModel @AssistedInject   constructor(@Assisted val album: Album): ViewModel()
{
    @Inject
    lateinit var songsDataManagerFactory: ISongsDataManagerFactory

    companion object const val PAGE_SIZE = 10
    val songs = Pager(config = PagingConfig(pageSize = PAGE_SIZE,initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            SongsPagingSource(songsDataManagerFactory.create(album))

        })
        .flow.cachedIn(viewModelScope)


    @Suppress("UNCHECKED_CAST")
    class ProviderFactory(private val songsViewModelFactory: ISongsViewModelFactory, private val album: Album) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return songsViewModelFactory.create(album) as T
        }
    }


    @AssistedFactory
    interface ISongsViewModelFactory {
        fun create(album: Album): SongsViewModel
    }

}