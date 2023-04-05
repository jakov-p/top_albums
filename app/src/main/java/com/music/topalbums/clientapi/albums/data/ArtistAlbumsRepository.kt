package com.music.topalbums.clientapi.albums.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistAlbumsRepository(val artistId : Int) : BasicAlbumsRepository()
{
    override suspend fun loadAlbums(onFinished: () -> Unit)
    {
        clientApi.getArtistAlbums(artistId)?.let {
            albumCollection = AlbumCollection(it)
            onFinished()
        }?: throw Exception()
    }
}