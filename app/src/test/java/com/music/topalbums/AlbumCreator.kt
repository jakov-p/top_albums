package com.music.topalbums

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import com.music.topalbums.utilities.DateConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object AlbumCreator
{
    fun createAlbum(genre: AlbumFilter.Genre? = null, releaseDate: LocalDateTime? = null): Album
    {
        return createAlbum(null, null, null,  genre ,  releaseDate)
    }

    fun createAlbum(collectionId:Int?, collectionName:String?, artistName:String?,  genre: AlbumFilter.Genre? ,  releaseDate: LocalDateTime?): Album
    {
        return Album(
            originalPos = 0,
            artistName =  artistName,
            artistViewUrl = null,

            collectionImageUrl = null,
            collectionViewUrl = null,

            primaryGenreName = genre?.name?.capitalize(),
            primaryGenreId = genre?.value,

            collectionId = collectionId ?: 0,
            collectionName = collectionName,
            collectionPrice = 100.0f,
            currency = "dollar",

            trackCount = 10,
            releaseDate = releaseDate?.let { DateConverter.fromDateToString(it) }
        )
    }
}