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
class OnlyOneItemInSmallListTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        val allScenario = ScenarioPerformer()

        //the small list does not have enough items to fill a full page (only one item in the small list)
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 1)
                addAlbums(false, 0, 1, 1)
            },
            allScenario.createLoadParams(6, 0),
            PagingSource.LoadResult.Page(createList(1), null, 1, 0, 0)
        )

        //the small list offers zero items for the second page  --> waiting for the full list
        allScenario.runSingle(
            {
                addIsFullyLoaded(false)
                addAlbumsCount(false, 1)
                addAlbumsCount(true, 17)
                addIsFullyLoaded(true)
                addAlbums(true, 1, 7, 6)
            },
            allScenario.createLoadParams(6, 1),
            PagingSource.LoadResult.Page(createList(6), 0, 2, 1, 10)
        )

        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 17)
                addIsFullyLoaded(true)
                addAlbums(true, 7, 13, 6)
            },
            allScenario.createLoadParams(6, 2),
            PagingSource.LoadResult.Page(createList(6), 1, 3, 7, 4)
        )

        //the last page is not filled to full
        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 17)
                addIsFullyLoaded(true)
                addAlbums(true, 13, 17, 4)
            },
            allScenario.createLoadParams(6, 3),
            PagingSource.LoadResult.Page(createList(4), 2, null, 13, 0)
        )
    }
}