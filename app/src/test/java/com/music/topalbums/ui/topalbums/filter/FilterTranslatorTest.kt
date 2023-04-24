package com.music.topalbums.ui.topalbums.filter

import com.music.topalbums.AlbumCreator
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalDateTime

@RunWith(JUnit4::class)
class FilterTranslatorTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun `when empty filter and search is null expect result ok`()
    {
        val filterTranslator = FilterTranslator(AlbumFilter.EMPTY, null)

        val album1: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK)
        assertTrue(filterTranslator.check(album1))

        val album2: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now())
        assertTrue(filterTranslator.check(album2))

        val album3: Album = AlbumCreator.createAlbum(null, null, null, null, null)
        assertTrue(filterTranslator.check(album3))
    }

    @Test
    fun `when only genre in filter`()
    {
        val filterTranslator = FilterTranslator(AlbumFilter(AlbumFilter.Genre.ROCK, null), null)

        val album1: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK)
        assertTrue(filterTranslator.check(album1))

        val album11: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now())
        assertTrue(filterTranslator.check(album11))

        val album2: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.POP)
        assertFalse(filterTranslator.check(album2))

        val album22: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.POP, LocalDateTime.now())
        assertFalse(filterTranslator.check(album22))

        val album3: Album = AlbumCreator.createAlbum(null, null)
        assertFalse(filterTranslator.check(album3))
    }

    @Test
    fun `when only release time in filter`()
    {
        val filterTranslator = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), null)

        val album1: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, null)
        assertFalse(filterTranslator.check(album1))

        val album11: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now().minusDays(4))
        assertTrue(filterTranslator.check(album11))

        val album12: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now().minusYears(4))
        assertFalse(filterTranslator.check(album12))


        val album2: Album = AlbumCreator.createAlbum(null, null)
        assertFalse(filterTranslator.check(album2))

        val album22: Album = AlbumCreator.createAlbum(null, LocalDateTime.now().minusYears(5))
        assertFalse(filterTranslator.check(album22))
    }


    @Test
    fun `when genre and release time in filter`()
    {
        val filterTranslator = FilterTranslator(AlbumFilter(AlbumFilter.Genre.ROCK, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), null)

        val album1: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, null)
        assertFalse(filterTranslator.check(album1))

        val album11: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now().minusDays(4))
        assertTrue(filterTranslator.check(album11))

        val album12: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.ROCK, LocalDateTime.now().minusDays(400))
        assertFalse(filterTranslator.check(album12))


        val album2: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.POP, null)
        assertFalse(filterTranslator.check(album2))

        val album21: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.POP, LocalDateTime.now().minusDays(4))
        assertFalse(filterTranslator.check(album21))

        val album22: Album = AlbumCreator.createAlbum(AlbumFilter.Genre.POP, LocalDateTime.now().minusDays(400))
        assertFalse(filterTranslator.check(album22))


        val album3: Album = AlbumCreator.createAlbum(null, null)
        assertFalse(filterTranslator.check(album3))
    }

    @Test
    fun `test ReleaseTimeCriteria`()
    {
        val filterTranslator1 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), null)
        assertTrue(filterTranslator1.check(albumNow({ minusDays(5) })))
        assertFalse(filterTranslator1.check(albumNow({ minusYears(5) })))

        val filterTranslator2 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_MONTH), null)
        assertTrue(filterTranslator2.check(albumNow({ minusDays(5) })))
        assertFalse(filterTranslator2.check(albumNow({ minusMonths(5) })))


        val filterTranslator3 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_WEEK), null)
        assertTrue(filterTranslator3.check(albumNow({ minusDays(1) })))
        assertFalse(filterTranslator3.check(albumNow({ minusYears(15) })))


        val filterTranslator4 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_ONE_YEAR), null)
        assertTrue(filterTranslator4.check(albumNow({ minusYears(2) })))
        assertFalse(filterTranslator4.check(albumNow({ minusMonths(5) })))


        val filterTranslator5 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_FIVE_YEARS), null)
        assertTrue(filterTranslator5.check(albumNow({ minusYears(20) })))
        assertFalse(filterTranslator5.check(albumNow({ minusYears(1) })))
    }

    private fun albumNow(block: LocalDateTime.() -> LocalDateTime): Album = AlbumCreator.createAlbum(null, LocalDateTime.now().block())


    @Test
    fun `when only search`()
    {
        val album1: Album = AlbumCreator.createAlbum(null, "My first album", "The best singer" , null, null, )

        val filterTranslator11 = FilterTranslator(AlbumFilter.EMPTY, "first")
        assertTrue(filterTranslator11.check(album1))

        val filterTranslator12 = FilterTranslator(AlbumFilter.EMPTY, "First")
        assertTrue(filterTranslator12.check(album1))

        val filterTranslator13 = FilterTranslator(AlbumFilter.EMPTY, "fIRST")
        assertTrue(filterTranslator13.check(album1))

        val filterTranslator14 = FilterTranslator(AlbumFilter.EMPTY, " fIRST aLBu")
        assertTrue(filterTranslator14.check(album1))



        val filterTranslator21 = FilterTranslator(AlbumFilter.EMPTY, "best")
        assertTrue(filterTranslator21.check(album1))

        val filterTranslator22 = FilterTranslator(AlbumFilter.EMPTY, "Best")
        assertTrue(filterTranslator22.check(album1))

        val filterTranslator23 = FilterTranslator(AlbumFilter.EMPTY, "bEsT")
        assertTrue(filterTranslator23.check(album1))

        val filterTranslator24 = FilterTranslator(AlbumFilter.EMPTY, " bESt sinGe")
        assertTrue(filterTranslator24.check(album1))


        val filterTranslator31 = FilterTranslator(AlbumFilter.EMPTY, "albuu")
        assertFalse(filterTranslator31.check(album1))
    }

    @Test
    fun `when only search unusual case `()
    {
        val filterTranslator = FilterTranslator(AlbumFilter.EMPTY, "rock")

        val album1: Album = AlbumCreator.createAlbum(null, null, null , null, null, )
        assertTrue(filterTranslator.check(album1))

        val album2: Album = AlbumCreator.createAlbum(null, "aaa", "bbb" , AlbumFilter.Genre.ROCK, null, )
        assertFalse(filterTranslator.check(album2))
    }


    @Test
    fun `when filter and search`()
    {
        val album1: Album = AlbumCreator.createAlbum(null, "My first album", "The best singer"
            , AlbumFilter.Genre.ROCK, LocalDateTime.now().minusMonths(5) )

        val filterTranslator11 = FilterTranslator(AlbumFilter(AlbumFilter.Genre.ROCK, null), "first")
        assertTrue(filterTranslator11.check(album1))

        val filterTranslator12 = FilterTranslator(AlbumFilter(AlbumFilter.Genre.ROCK, null), "firstA")
        assertFalse(filterTranslator12.check(album1))

        val filterTranslator13 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), "first")
        assertTrue(filterTranslator13.check(album1))

        val filterTranslator14 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_WEEK), "first")
        assertFalse(filterTranslator14.check(album1))

        val filterTranslator15 = FilterTranslator(AlbumFilter(null, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), "firstA")
        assertFalse(filterTranslator15.check(album1))


        val filterTranslator16 = FilterTranslator(AlbumFilter(AlbumFilter.Genre.ROCK, AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR), "first")
        assertTrue(filterTranslator16.check(album1))
    }
}


