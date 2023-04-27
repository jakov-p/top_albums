package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Song
import com.music.topalbums.clientapi.collection.SongCollection

object Utilities
{
    fun createSongCollection(fromPos:Int, toPos: Int ): SongCollection
    {
        val songs = mutableListOf<Song>().apply {
            repeat(toPos - fromPos ) {
                add(createSong(fromPos + it))
            }
        }
        return SongCollection(songs)
    }

    fun createSong(trackId:Int ): Song
    {
        return Song(
            artistId = 164449,
            collectionId = 1659426282,
            amgArtistId = null,
            artistName = "Everything But the Girl",
            country = "USA",
            collectionName = "Fuse",
            artistViewUrl = null,
            collectionPrice= 9.99f,
            currency = "USD",
            collectionExplicitness = "notExplicit",
            releaseDate = "2023-04-21T12:00:00Z",  //Date
            primaryGenreName = "Pop",
            trackCount = 10,
            trackId = trackId,
            trackName= "When You Mess Up",
            trackCensoredName= "When You Mess Up",
            trackViewUrl= null,
            previewUrl= null,
            trackPrice= 1.29f,
            trackExplicitness = "notExplicit",
            trackNumber = 1,
            trackTimeMillis = 228467,
            isStreamable= true)
    }


}