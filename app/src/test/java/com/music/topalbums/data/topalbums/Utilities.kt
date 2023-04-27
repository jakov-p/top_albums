package com.music.topalbums.data.topalbums

import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository
import kotlinx.coroutines.runBlocking
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

object Utilities
{
    fun createAlbumCollection(fromPos:Int, toPos: Int ): AlbumCollection
    {
        val albums = mutableListOf<Album>().apply {
            repeat(toPos - fromPos ) {
                add(createAlbum(fromPos + it))
            }
        }
        return AlbumCollection(albums)
    }

    fun createAlbum(originalPos:Int ): Album
    {
        return Album(
            originalPos = originalPos,
            artistName = "Metallica",
            artistViewUrl = null,

            collectionImageUrl = null,
            collectionViewUrl = null,

            primaryGenreName = "Heavy Metal",
            primaryGenreId = 1153,

            collectionId = 1655432387,
            collectionName = "72 Seasons - Metallica",
            collectionPrice = 11.99f,
            currency = "USD",

            trackCount = 10,
            releaseDate = "2023-04-14T00:00:00-07:00")
    }


}