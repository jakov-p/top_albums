package com.music.topalbums.ui.topalbums.albums_paging_source

import androidx.paging.PagingSource
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import com.music.topalbums.ui.topalbums.albums_paging_source.ScenarioPerformer.Companion.createList
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OnlyOnceSmallListTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        //the small list has enough items to fill a full page
        val allScenario = ScenarioPerformer()
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 8)
                addAlbums(false, 0, 6, 6)
            },
            allScenario.createLoadParams(6, null),
            PagingSource.LoadResult.Page(createList(6), null, 1, 0, 2)
        )

        //the full list is already available, so take it from it
        allScenario.runSingle(
            {
                addAlbumsCount(true, 78)
                addIsFullyLoaded(true)
                addAlbums(true, 6, 12, 6)
            },
            allScenario.createLoadParams(6, 1),
            PagingSource.LoadResult.Page(createList(6), 0, 2, 6, 66)
        )

        allScenario.runSingle(
            {
                addAlbumsCount(true, 78)
                addIsFullyLoaded(true)
                addAlbums(true, 12, 18, 6)
            },
            allScenario.createLoadParams(6, 2),
            PagingSource.LoadResult.Page(createList(6), 1, 3, 12, 60)
        )

        allScenario.runSingle(
            {
                addAlbumsCount(true, 78)
                addIsFullyLoaded(true)
                addAlbums(true, 18, 24, 6)
            },
            allScenario.createLoadParams(6, 3),
            PagingSource.LoadResult.Page(createList(6), 2, 4, 18, 54)
        )

        allScenario.runSingle(
            {
                addAlbumsCount(true, 78)
                addIsFullyLoaded(true)
                addAlbums(true, 24, 30, 6)
            },
            allScenario.createLoadParams(6, 4),
            PagingSource.LoadResult.Page(createList(6), 3, 5, 24, 48)
        )

        allScenario.runSingle(
            {
                addAlbumsCount(true, 78)
                addIsFullyLoaded(true)
                addAlbums(true, 30, 36, 6)
            },
            allScenario.createLoadParams(6, 5),
            PagingSource.LoadResult.Page(createList(6), 4, 6, 30, 42)
        )
    }

}