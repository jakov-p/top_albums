package com.music.topalbums.clientapi.retrofit

import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.retrofit.utilities.CallPerformer
import com.music.topalbums.clientapi.retrofit.model.AlbumSongsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistSongsCollection
import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.clientapi.collection.SongCollection
import javax.inject.Inject


class  ClientApi @Inject constructor(): IClientApi
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
    override suspend fun getTopAlbums(country: String, limit: Int): AlbumCollection?
    {
        val runner = CallPerformer("Get Top Albums Request"){ ServiceApi.getTopAlbums(country, limit) }
        val topAlbumsCollection: TopAlbumsCollection? = runner.perform()
        return topAlbumsCollection?.let { AlbumCollection(it) }

    }

    /**
     * Get all the songs of a particular album
     * @param albumId unique album's id on the Server
     * @return the whole collection of songs
     */
    override suspend fun getAlbumSongs(albumId : Int): SongCollection?
    {
        val runner = CallPerformer("Get Songs On Album Request"){ ServiceApi.getAlbumSongs(albumId) }
        val albumSongsCollection: AlbumSongsCollection? = runner.perform()
        return albumSongsCollection?.let { SongCollection(it) }
    }


    /**
     * Get all the albums  of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of albums
     */
    override suspend fun getArtistAlbums(artistId : Int): AlbumCollection?
    {
        val runner = CallPerformer("Get Albums Of Artist Request"){ ServiceApi.getArtistAlbums(artistId) }
        val artistAlbumsCollection: ArtistAlbumsCollection? = runner.perform()
        return artistAlbumsCollection?.let { AlbumCollection(it) }
    }

    /**
     * Get all the songs of a particular singer (band)
     * @param artistId unique artist's id on the Server
     * @return the whole collection of songs
     */
    override suspend fun getArtistSongs(artistId : Int): SongCollection?
    {
        val runner = CallPerformer("Get Songs Of Artist Request"){ ServiceApi.getArtistSongs(artistId) }
        val artistSongsCollection: ArtistSongsCollection? =  runner.perform()
        return artistSongsCollection?.let { SongCollection(it) }
    }

}