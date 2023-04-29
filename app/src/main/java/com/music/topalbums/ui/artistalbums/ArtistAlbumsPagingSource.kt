package com.music.topalbums.ui.artistalbums

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.albums.artistalbums.IArtistAlbumsDataManager
import com.music.topalbums.ui.common.SimpleItemsPagingSource

/**
 * Provides artist albums data to be shown in a recycle view list
 * Supports paging.
 *
 * @param artistAlbumsDataManager
 */
class ArtistAlbumsPagingSource(artistAlbumsDataManager: IArtistAlbumsDataManager) :
    SimpleItemsPagingSource<Album>(artistAlbumsDataManager::getAlbumsCount, artistAlbumsDataManager::getAlbums )
