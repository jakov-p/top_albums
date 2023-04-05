package com.music.topalbums.clientapi.albums.data

import com.music.topalbums.clientapi.albums.ClientApi
import com.music.topalbums.clientapi.albums.model.AlbumSongsCollection
import com.music.topalbums.logger.Logger.loggable

abstract class BasicAlbumsRepository
{
    val TAG = BasicAlbumsRepository::class.java.simpleName

    protected val clientApi: ClientApi = ClientApi
    protected  lateinit var albumCollection: AlbumCollection

    suspend abstract fun loadAlbums(onFinished: () -> Unit)

    fun getAlbumsCount(): Int
    {
        return albumCollection.list.size
    }

    fun getAlbumsWithSongs(fromIndex:Int, toIndex: Int): List<AlbumWithSongs>
    {
        loggable.i(TAG, "Fetching albums from $fromIndex to $toIndex ...")

        return (fromIndex..toIndex - 1).map {
            val album = albumCollection.list[it]
            loggable.i(TAG, "Fetching album with index = $it ...")
            AlbumWithSongs(album, getSingleAlbumSongs(album))
        }.
        also {
            loggable.i(TAG, "Fetched albums, in total =  ${it.size}")
        }
    }


    protected fun getSingleAlbumSongs(album: Album): List<Song>
    {
        loggable.i(TAG, "Fetching songs for an album, album = $album ...")
        album.collectionId?.let {

            //val albumSongsCollection = clientApi.getAlbumSongs(albumId = it)
            val albumSongsCollection = AlbumSongsCollection()
            albumSongsCollection.resultCount = 0
            albumSongsCollection.results = ArrayList()

            return SongCollection(albumSongsCollection!!).list
                .also {
                    loggable.i(TAG, "Fetched songs for an album, total track number = ${it.size}")
                }
        } ?:
            throw Exception()
    }

    data class AlbumWithSongs(val album: Album, val songs: List<Song>)
    {
        val artistId: Int? = if (songs.size > 0) songs[0].artistId else null
    }
}