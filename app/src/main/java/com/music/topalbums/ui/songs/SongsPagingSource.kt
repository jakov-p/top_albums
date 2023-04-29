package com.music.topalbums.ui.songs

import com.music.topalbums.clientapi.collection.Song
import com.music.topalbums.data.songs.ISongsDataManager
import com.music.topalbums.ui.common.SimpleItemsPagingSource

/**
 * Provides songs data to be shown in a recycle view list
 * Supports paging.
 *
 * @property songsDataManager
 */
class SongsPagingSource(val songsDataManager: ISongsDataManager) :
    SimpleItemsPagingSource<Song>(songsDataManager::getSongsCount, songsDataManager::getSongs )
