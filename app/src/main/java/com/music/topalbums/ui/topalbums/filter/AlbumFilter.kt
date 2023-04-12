package com.music.topalbums.ui.topalbums.filter


/*
    <string name="pop_genre">Pop</string>
    <string name="alternative_genre">Alternative</string>
    <string name="rock_genre">Rock</string>
    <string name="country_genre">Country</string>
    <string name="christian_genre">Christian &amp; Gospel</string>
    <string name="classical_genre">Classical</string>
    <string name="singer_genre">Singer/Songwriter</string>
    <string name="hard_rock_genre">Hard Rock</string>
    <string name="musicals_genre">Musicals</string>
    <string name="soundtrack_genre">Soundtrack</string>

    <string name="one_day_time">Older Than 1 Day</string>
    <string name="one_month_time">Older Than 1 Month</string>
    <string name="one_year_time">Older Than 1 Year</string>
 */
data class AlbumFilter(val genre: Genre?, val releaseTime: ReleaseTime?)
{
    enum class ReleaseTime
    {
        NEWER_THAN_ONE_WEEK,
        NEWER_THAN_ONE_MONTH,
        NEWER_THAN_ONE_YEAR,
        OLDER_OVER_ONE_YEAR,
        OLDER_OVER_FIVE_YEARS,
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

}