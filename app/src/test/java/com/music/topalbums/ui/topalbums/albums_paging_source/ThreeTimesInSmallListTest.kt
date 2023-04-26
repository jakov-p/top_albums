package com.music.topalbums.ui.topalbums.albums_paging_source

import androidx.paging.PagingSource
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import com.music.topalbums.ui.topalbums.albums_paging_source.ScenarioPerformer.Companion.createList
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

//in big to end
@RunWith(JUnit4::class)
class ThreeTimesInSmallListTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        val allScenario = ScenarioPerformer()

        //the small list is available only , enough to fill the full first page
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 8)
                addAlbums(false, 0, 6, 6)
            },
            allScenario.createLoadParams(6, 0),
            PagingSource.LoadResult.Page(createList(6), null, 1, 0, 2)
         )

        //the small list is available only , enough to fill the second page only partially
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 8)
                addAlbums(false, 6, 8, 2)
            },
            allScenario.createLoadParams(6, 1),
            PagingSource.LoadResult.Page(createList(2), 0, 2, 6, 0)
        )

        //the small list offers zero items for the third page  --> waiting for the full list
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 8)
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 8, 14, 6)
            },
            allScenario.createLoadParams(6, 2),
            PagingSource.LoadResult.Page(createList(6), 1, 3, 8, 56)
         )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 14, 20, 6)
            },
            allScenario.createLoadParams(6, 3),
            PagingSource.LoadResult.Page(createList(6), 2, 4, 14, 50)
         )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 20, 26, 6)
            },
            allScenario.createLoadParams(6, 4),
            PagingSource.LoadResult.Page(createList(6), 3, 5, 20, 44)
         )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 26, 32, 6)
            },
            allScenario.createLoadParams(6, 5),
            PagingSource.LoadResult.Page(createList(6), 4, 6, 26, 38)
        )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 32, 38, 6)
            },
            allScenario.createLoadParams(6, 6),
            PagingSource.LoadResult.Page(createList(6), 5, 7, 32, 32)
         )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 38, 44, 6)
            },
            allScenario.createLoadParams(6, 7),
            PagingSource.LoadResult.Page(createList(6), 6, 8, 38, 26)
         )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 44, 50, 6)
            },
            allScenario.createLoadParams(6, 8),
            PagingSource.LoadResult.Page(createList(6), 7, 9, 44, 20)
        )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 50, 56, 6)
            },
            allScenario.createLoadParams(6, 9),
            PagingSource.LoadResult.Page(createList(6), 8, 10, 50, 14)
        )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 56, 62, 6)
            },
            allScenario.createLoadParams(6, 10),
            PagingSource.LoadResult.Page(createList(6), 9, 11, 56, 8)
        )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 62, 68, 6)
            },
            allScenario.createLoadParams(6, 11),
            PagingSource.LoadResult.Page(createList(6), 10, 12, 62, 2)
        )

         allScenario.runSingle(
            {
                addAlbumsCount(true, 70)
                addIsFullyLoaded(true)
                addAlbums(true, 68, 70, 2)
            },
            allScenario.createLoadParams(6, 12),
            PagingSource.LoadResult.Page(createList(2), 11, null, 68, 0)
        )
    }

}