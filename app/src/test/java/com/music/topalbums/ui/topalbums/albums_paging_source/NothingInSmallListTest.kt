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
class NothingInSmallListTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        val allScenario = ScenarioPerformer()

        //the small list is empty, so waiting for the full list
        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 21)
                addIsFullyLoaded(true)
                addAlbums(true, 0, 6, 6)
            },
            allScenario.createLoadParams(6, 0),
            PagingSource.LoadResult.Page(createList(6), null, 1, 0, 15)
        )

        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 21)
                addIsFullyLoaded(true)
                addAlbums(true, 6, 12, 6)
            },
            allScenario.createLoadParams(6, 1),
            PagingSource.LoadResult.Page(createList(6), 0, 2, 6, 9)
        )

        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 21)
                addIsFullyLoaded(true)
                addAlbums(true, 12, 18, 6)
            },
            allScenario.createLoadParams(6, 2),
            PagingSource.LoadResult.Page(createList(6), 1, 3, 12, 3)
        )


        allScenario.runSingle(
            {
                addIsFullyLoaded(true)
                addIsFullyLoaded(true)
                addAlbumsCount(true, 21)
                addIsFullyLoaded(true)
                addAlbums(true, 18, 21, 3)
            },
            allScenario.createLoadParams(6, 3),
            PagingSource.LoadResult.Page(createList(3), 2, null, 18, 0)
        )
    }
}