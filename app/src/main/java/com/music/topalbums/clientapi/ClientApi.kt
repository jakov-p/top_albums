package com.music.topalbums.clientapi

import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.utilities.CallPerformer
import com.music.topalbums.clientapi.model.AlbumSongsCollection
import com.music.topalbums.clientapi.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.model.ArtistSongsCollection


object ClientApi
{
    val TAG = ClientApi::class.java.simpleName

    /**
     * Get the top albums list currently in a particular country
     *
     * @param country the short name of the country whose album list is to be downloaded
     *           ('us' for United States, 'fr' for France,...)
     * @param limit  The maximum number of albums to download (it seems that always less than 100
     *           albums are in a country's Top album List on the internet, often below 50 )
     */
    suspend fun getTopAlbums( country: String, limit: Int): TopAlbumsCollection?
    {
        val callPerformer = CallPerformer("Get Top Albums Request"){ ServiceApi.getTopAlbums(country, limit) }
        return callPerformer.perform()
    }

    /**
     * Get all the songs of a particular album
     * @param albumId unique album's id on the Server
     * @return the whole collection of songs
     */
    suspend fun getAlbumSongs(albumId : Int): AlbumSongsCollection?
    {
        val runner = CallPerformer("Get Songs On Album Request"){ ServiceApi.getAlbumSongs(albumId) }
        return runner.perform()
    }

    /**
     * Get all the albums  of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of albums
     */
    suspend fun getArtistAlbums(artistId : Int): ArtistAlbumsCollection?
    {
        val runner = CallPerformer("Get Albums Of Artist Request"){ ServiceApi.getArtistAlbums(artistId) }
        return runner.perform()
    }

    /**
     * Get all the songs of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of songs
     */
    suspend fun getArtistSongs(artistId : Int): ArtistSongsCollection?
    {
        val runner = CallPerformer("Get Songs Of Artist Request"){ ServiceApi.getArtistSongs(artistId) }
        return runner.perform()
    }

}