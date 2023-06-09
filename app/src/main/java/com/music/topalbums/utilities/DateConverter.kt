package com.music.topalbums.utilities

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/***
 * From a local date time to a string
 * From a string into a local date time
 */
object DateConverter
{
    //e.g. "2011-04-12T07:00:00Z" --> local date time
    fun fromStringToDate(stringDate:String): LocalDateTime
    {
        val zonedDateTime = ZonedDateTime.parse(stringDate, DateTimeFormatter.ISO_ZONED_DATE_TIME)

        val swissZone: ZoneId = ZoneId.of("Europe/Zurich")
        val swissZoned: ZonedDateTime = zonedDateTime.withZoneSameInstant(swissZone)
        return swissZoned.toLocalDateTime()
    }

    //e.g. local date time --> "2011-04-12T07:00:00Z"
    fun fromDateToString(localDateTime:LocalDateTime): String
    {
        return localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }

}