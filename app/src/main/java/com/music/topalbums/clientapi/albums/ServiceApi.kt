package com.music.topalbums.clientapi.albums

import com.music.topalbums.clientapi.albums.model.AlbumSongsCollection
import com.music.topalbums.clientapi.albums.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.albums.model.ArtistSongsCollection
import retrofit2.Response

object ServiceApi
{
    val retrofitClient: RetrofitClient = RetrofitClient()

    suspend fun getTopAlbums(country: String, limit: Int): Response<TopAlbumsCollection>
    {
        return  retrofitClient.service.getTopAlbums(country, limit)
    }

    suspend fun getAlbumSongs(albumId: Int): Response<AlbumSongsCollection>
    {
        return  retrofitClient.service.getAlbumSongs(albumId)
    }

    suspend fun getArtistAlbums(artistId: Int): Response<ArtistAlbumsCollection>
    {
        return  retrofitClient.service.getArtistAlbums(artistId)
    }

    suspend fun getArtistSongs(artistId: Int): Response<ArtistSongsCollection>
    {
        return  retrofitClient.service.getArtistSongs(artistId)
    }


}