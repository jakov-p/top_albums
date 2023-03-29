package com.music.topalbums.clientapi.albums

import com.music.topalbums.clientapi.CallPerformer
import com.music.topalbums.clientapi.albums.model.AlbumSongsCollection
import com.music.topalbums.clientapi.albums.model.ArtistAlbumsCollection
import com.music.topalbums.clientapi.albums.model.ArtistSongsCollection


object Repository
{
    val TAG = Repository::class.java.simpleName

    suspend fun getTopAlbums( country: String, limit: Int): TopAlbumsCollection?
    {
        val callPerformer = CallPerformer("Get Top Albums Request"){ ServiceApi.getTopAlbums(country, limit) }
        return callPerformer.perform()
    }

    suspend fun getAlbumSongs(albumId : Int): AlbumSongsCollection?
    {
        val runner = CallPerformer("Get Songs On Album Request"){ ServiceApi.getAlbumSongs(albumId) }
        return runner.perform()
    }

    suspend fun getArtistAlbums(artistId : Int): ArtistAlbumsCollection?
    {
        val runner = CallPerformer("Get Albums Of Artist Request"){ ServiceApi.getArtistAlbums(artistId) }
        return runner.perform()
    }


    suspend fun getArtistSongs(artistId : Int): ArtistSongsCollection?
    {
        val runner = CallPerformer("Get Songs Of Artist Request"){ ServiceApi.getArtistSongs(artistId) }
        return runner.perform()
    }

}