package com.music.topalbums.ui.topalbums.filter

import javax.inject.Inject

/**
 * filter defining the criteria for an album to be shown in  GUI
 *
 * @constructor Create an empty Album filter (any album would 'pass' this filter)
 * @param genre pop, rock, alternative,... (not all possible music genres are here - they
 *                would take too much space when shown in GUI), if null then ignored
 * @param releaseTimeCriteria - the period in which the album was released ("how old the album is?"),
 *               if null then ignored
 */
data class AlbumFilter @Inject constructor (val genre: Genre?, val releaseTimeCriteria: ReleaseTimeCriteria?)
{
    fun isEmpty() = (genre == null && releaseTimeCriteria == null)

    enum class ReleaseTimeCriteria
    {
        NEWER_THAN_ONE_WEEK, //album is released within the last week
        NEWER_THAN_ONE_MONTH, //album is released within the last month
        NEWER_THAN_ONE_YEAR, //album is released within the last year
        OLDER_OVER_ONE_YEAR, //album is released more than a year ago
        OLDER_OVER_FIVE_YEARS, //album is released more than 5 years ago
    }

    enum class  Genre(val value: Int)
    {
        POP(14),
        ALTERNATIVE(20),
        ROCK(21),
        COUNTRY(6),
        CHRISTIAN_AND_GOSPEL(22),
        CLASSICAL(5),
        SINGER_OR_SONGWRITER(10),
        HARD_ROCK(1152),
        MUSICALS(1166),
        SOUNDTRACK(16),
    }

    companion object
    {
        //any album will pass this filter (as if there is no filter)
        val EMPTY =  AlbumFilter(null, null)
    }

}