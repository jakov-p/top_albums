package com.music.topalbums.data.albums

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