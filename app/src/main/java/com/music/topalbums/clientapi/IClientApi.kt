package com.music.topalbums.clientapi

import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.clientapi.collection.SongCollection

interface IClientApi
{
    suspend fun getTopAlbums( country: String, limit: Int): AlbumCollection?
    suspend fun getAlbumSongs(albumId : Int): SongCollection?
    suspend fun getArtistAlbums(artistId : Int): AlbumCollection?
    suspend fun getArtistSongs(artistId : Int): SongCollection?
}