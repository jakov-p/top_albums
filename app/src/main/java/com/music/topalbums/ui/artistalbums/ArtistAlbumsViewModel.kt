package com.music.topalbums.ui.artistalbums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.music.topalbums.data.albums.artistalbums.IArtistAlbumsDataManager
import com.music.topalbums.data.albums.artistalbums.IArtistAlbumsDataManagerFactory
import com.music.topalbums.ui.common.SimpleItemsPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

/**
 * An artist's albums view model
 *
 * Feeds the GUI with the album list for a particular artist
 */
class ArtistAlbumsViewModel @AssistedInject   constructor(@Assisted val artistId: Int): ViewModel()
{
    @Inject
    lateinit var artistAlbumsDataManagerFactory: IArtistAlbumsDataManagerFactory

    companion object const val PAGE_SIZE = 10
    val albums = Pager(config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE ,prefetchDistance = PAGE_SIZE,
                                    jumpThreshold = PAGE_SIZE * 3, enablePlaceholders = true),
        pagingSourceFactory = {
            ArtistAlbumsPagingSource(artistAlbumsDataManagerFactory.create(artistId))
        })
        .flow.cachedIn(viewModelScope)


    @Suppress("UNCHECKED_CAST")
    class ProviderFactory(private val artistAlbumsViewModelFactory: IArtistAlbumsViewModelFactory, private val artistId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return artistAlbumsViewModelFactory.create(artistId) as T
        }
    }


    @AssistedFactory
    interface IArtistAlbumsViewModelFactory {
        fun create(artistId: Int): ArtistAlbumsViewModel
    }
}