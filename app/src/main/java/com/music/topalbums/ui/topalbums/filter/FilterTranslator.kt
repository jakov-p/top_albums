package com.music.topalbums.ui.topalbums.filter

import com.music.topalbums.data.albums.Album
import com.music.topalbums.logger.Logger.loggable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Converts an 'AlbumFilter' object combined with a search string into a java method that can
 * be applied to an Album.
 *
 * @param albumFilter genre + release time criteria
 * @param searchText text to be contained in the album fields (artist name field and album name field))
 */
class FilterTranslator(val albumFilter : AlbumFilter, val searchText: String?)
{
    val TAG = FilterTranslator::class.java.simpleName

    /**
     * Does this album pass both filter and search text criteria?
     * @param album
     * @return
     */
    fun check(album: Album): Boolean
    {
        //it fails if genre must be checked and the album is not of that particular genre
        if(albumFilter.genre!=null)
        {
            if (album.primaryGenreId != albumFilter.genre.value)
            {
                return false
            }
        }

        //it fails if release date must be checked and the album's release date is not in the expected date range
        if(album.releaseDate!=null && albumFilter.releaseTimeCriteria!=null)
        {
            val albumReleaseDate = fromStrToDate(album.releaseDate)
            val (compareLocalTime, isAfter) = calculateCompareDate(albumFilter.releaseTimeCriteria)
            if(!isAfter && !albumReleaseDate.isAfter(compareLocalTime))
            {
                return false
            }
            else if(isAfter && !albumReleaseDate.isBefore(compareLocalTime))
            {
                return false
            }
        }

        //it fails if search text must be checked and neither the album's artist name field nor
        //the album's name field contains the searched text (case is ignored)
        searchText?.lowercase()?.let{
            val isInsideCollectionName = album.collectionName?.lowercase()?.contains(it)?:true
            val isInsideArtistName = album.artistName?.lowercase()?.contains(it)?:true
            if(!isInsideCollectionName && !isInsideArtistName)
            {
                return false;
            }
        }

        //survived all the checks
        loggable.d(TAG, "Filtered album: ${album}")
        return true
    }


    //e.g. "2011-04-12T07:00:00Z" --> local date time
    private fun fromStrToDate(stringDate:String): LocalDateTime
    {
        val zonedDateTime = ZonedDateTime.parse(stringDate, DateTimeFormatter.ISO_ZONED_DATE_TIME)

        val swissZone: ZoneId = ZoneId.of("Europe/Zurich")
        val swissZoned: ZonedDateTime = zonedDateTime.withZoneSameInstant(swissZone)
        return swissZoned.toLocalDateTime()
    }

    /**
     * Calculate the compare date to be checked against the release date
     *
     * @param releaseTimeCriteria e.g. within the last year, within the last month,...
     * @return the calculated date to be used for comparison + boolean value telling if the range
     * before or after this calculated date will be taken into account
     */
    private fun calculateCompareDate(releaseTimeCriteria: AlbumFilter.ReleaseTimeCriteria): Pair<LocalDateTime, Boolean>
    {
        val currentDate: LocalDateTime = LocalDateTime.now()
        return when(releaseTimeCriteria)
        {
            AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_WEEK -> currentDate.minusDays(1) to false
            AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_MONTH -> currentDate.minusMonths(1)  to false
            AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR -> currentDate.minusYears(1)  to false

            AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_ONE_YEAR -> currentDate.minusYears(1)  to true
            AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_FIVE_YEARS -> currentDate.minusYears(5)  to true
        }
    }
}

