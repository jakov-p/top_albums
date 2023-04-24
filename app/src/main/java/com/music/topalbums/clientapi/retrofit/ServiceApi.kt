package com.music.topalbums.clientapi.retrofit

import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.AlbumSongsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistSongsCollection
import retrofit2.Response
import javax.inject.Inject

class ServiceApi @Inject constructor (val retrofitClient: RetrofitClient): IServiceApi
{
    /**
     * Get the top albums list currently in a particular country
     *
     * @param country the short name of the country whose album list is to be downloaded
     *           ('us' for United States, 'fr' for France,...)
     * @param limit  The maximum number of albums to download (it seems that always less than 100
     *           albums are in a country's Top album List on the internet, often below 50 )
     */
    override suspend fun getTopAlbums(country: String, limit: Int): Response<TopAlbumsCollection>
    {
        return  retrofitClient.service.getTopAlbums(country, limit)
    }

    /**
     * Get all the songs of a particular album
     * @param albumId unique album's id on the Server
     * @return the whole collection of songs
     */
    override suspend fun getAlbumSongs(albumId: Int): Response<AlbumSongsCollection>
    {
        return  retrofitClient.service.getAlbumSongs(albumId)
    }

    /**
     * Get all the albums  of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of albums
     */
    override suspend fun getArtistAlbums(artistId: Int): Response<ArtistAlbumsCollection>
    {
        return  retrofitClient.service.getArtistAlbums(artistId)
    }

    /**
     * Get all the songs of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of songs
     */
    override suspend fun getArtistSongs(artistId: Int): Response<ArtistSongsCollection>
    {
        return  retrofitClient.service.getArtistSongs(artistId)
    }
}