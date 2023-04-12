package com.music.topalbums.ui.topalbums.filter

import com.music.topalbums.data.albums.Album
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FilterTranslator(val albumFilter : AlbumFilter, val searchText: String?)
{
    fun check(album: Album): Boolean
    {
        if(albumFilter.genre!=null)
        {
            if (album.primaryGenreId != albumFilter.genre.value)
            {
                return false
            }
        }

        if(album.releaseDate!=null && albumFilter.releaseTime!=null)
        {
            val albumReleaseDate = toDate(album.releaseDate)
            val (compareLocalTime, isAfter) = calculateCompareDate(albumFilter.releaseTime)
            if(!isAfter && !albumReleaseDate.isAfter(compareLocalTime))
            {
                return false
            }
            else if(isAfter && !albumReleaseDate.isBefore(compareLocalTime))
            {
                return false
            }
        }


        searchText?.lowercase()?.let{
            val isInsideCollectionName = album.collectionName?.lowercase()?.contains(it)?:true
            val isInsideArtistName = album.artistName?.lowercase()?.contains(it)?:true
            if(!isInsideCollectionName && !isInsideArtistName)
            {
                return false;
            }
        }

        println(album.toString())
        return true
    }

    private fun toDate(stringDate:String): LocalDateTime
    {
        val zonedDateTime = ZonedDateTime.parse(stringDate, DateTimeFormatter.ISO_ZONED_DATE_TIME)

        val swissZone: ZoneId = ZoneId.of("Europe/Zurich")
        val swissZoned: ZonedDateTime = zonedDateTime.withZoneSameInstant(swissZone)
        return swissZoned.toLocalDateTime()
    }

    private fun calculateCompareDate(releaseTime: AlbumFilter.ReleaseTime): Pair<LocalDateTime, Boolean>
    {
        val current: LocalDateTime = LocalDateTime.now()
        return when(releaseTime)
        {
            AlbumFilter.ReleaseTime.NEWER_THAN_ONE_WEEK -> current.minusDays(1) to false
            AlbumFilter.ReleaseTime.NEWER_THAN_ONE_MONTH -> current.minusMonths(1)  to false
            AlbumFilter.ReleaseTime.NEWER_THAN_ONE_YEAR -> current.minusYears(1)  to false

            AlbumFilter.ReleaseTime.OLDER_OVER_ONE_YEAR -> current.minusYears(1)  to true
            AlbumFilter.ReleaseTime.OLDER_OVER_FIVE_YEARS -> current.minusYears(5)  to true
        }
    }
}