package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.ArtistInfo
import com.music.topalbums.clientapi.collection.Song

interface ISongsDataManager
{
    suspend fun getSongsCount(): Int
    suspend fun getSongs(fromIndex:Int, toIndex: Int): List<Song>
    fun getArtistInfo(): ArtistInfo?
}

