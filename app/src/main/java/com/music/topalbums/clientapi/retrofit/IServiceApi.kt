package com.music.topalbums.clientapi.retrofit

import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.AlbumSongsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistSongsCollection
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface  IServiceApi
{
    //https://itunes.apple.com/us/rss/topalbums/limit=10/json}
    @GET("{country}/rss/topalbums/limit={limit}/json")
    suspend fun getTopAlbums(@Path("country") country: String, @Path("limit") limit: Int): Response<TopAlbumsCollection>

    //https://itunes.apple.com/lookup?id=1665320666&entity=song
    @GET("lookup?entity=song")
    suspend fun getAlbumSongs(@Query("id") albumId: Int): Response<AlbumSongsCollection>


    //https://itunes.apple.com/lookup?id=909253&entity=album
    @GET("lookup?entity=album")
    suspend fun getArtistAlbums(@Query("id") artistId: Int): Response<ArtistAlbumsCollection>


    //https://itunes.apple.com/lookup?id=909253&entity=song
    @GET("lookup?entity=song")
    suspend fun getArtistSongs(@Query("id") artistId: Int): Response<ArtistSongsCollection>
}