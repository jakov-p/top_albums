package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Album

fun interface ISongsDataManagerFactory
{
    fun create(album: Album): ISongsDataManager
}