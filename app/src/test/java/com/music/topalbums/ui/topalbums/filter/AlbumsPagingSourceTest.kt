package com.music.topalbums.ui.topalbums.filter

import androidx.paging.PagingSource
import com.music.topalbums.AlbumCreator
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManager
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import com.music.topalbums.ui.topalbums.AlbumsPagingSource
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.junit.Assert.*

//TODO WRITE TEST
@RunWith(JUnit4::class)
class AlbumsPagingSourceTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    //@Test
    fun simpleTest()
    {
        val allScenario = AllScenario()
        allScenario.runSingle(
            {
                addAlbumsCount(false, 6)
                addAlbums(false, 2, 3, 7)
                addIsFullyLoaded(true)
            },
            allScenario.createLoadParams(5, null),
            PagingSource.LoadResult.Page(listOf(), 3, 3, 4, 6)
        )
    }


    private class AllScenario
    {
        val topAlbumsDataManager = Mockito.mock(ITopAlbumsDataManager::class.java)
        val albumsPagingSource = AlbumsPagingSource(topAlbumsDataManager)

        fun runSingle(block: suspend AllScenario.() -> Unit, loadParams: PagingSource.LoadParams<Int>, expectedLoadResult: PagingSource.LoadResult.Page<Int, Album>)
        {
            runBlocking {
                Mockito.reset(topAlbumsDataManager)
                block()

                val loadResult: PagingSource.LoadResult<Int, Album> = this@AllScenario.albumsPagingSource.load(loadParams)
                compareResult(expectedLoadResult, loadResult as PagingSource.LoadResult.Page<Int, Album>)
            }
        }

        fun createLoadParams(loadSize: Int, key: Int?): PagingSource.LoadParams<Int>
        {
            val loadParams = Mockito.mock(PagingSource.LoadParams::class.java) as PagingSource.LoadParams<Int>
            Mockito.`when`(loadParams.loadSize).thenReturn(loadSize)
            Mockito.`when`(loadParams.key).thenReturn(key)
            return loadParams
        }


        fun compareResult(expected: PagingSource.LoadResult.Page<Int, Album>, actual: PagingSource.LoadResult.Page<Int, Album>)
        {
            assertEquals(expected.data.size, actual.data.size)
            assertEquals(expected.prevKey, actual.prevKey)
            assertEquals(expected.nextKey, actual.nextKey)
            assertEquals(expected.itemsBefore, actual.itemsBefore)
            assertEquals(expected.itemsAfter, actual.itemsAfter)
        }

        suspend fun addAlbumsCount(isForFullLoad: Boolean, result: Int) =
            Mockito.`when`(topAlbumsDataManager.getAlbumsCount(false)).thenReturn(6)

        suspend fun addAlbums(isForFullLoad: Boolean, fromIndex: Int, toIndex: Int, resultListSize: Int) =
            Mockito.`when`(topAlbumsDataManager.getAlbums(isForFullLoad, fromIndex, toIndex)).thenReturn(createList(7))

        fun addIsFullyLoaded(result: Boolean) =
            Mockito.`when`(topAlbumsDataManager.isFullyLoaded).thenReturn(result)

    }


    companion object
    {
        fun createList(size: Int): List<Album>
        {
            return mutableListOf<Album>().apply {
                repeat(size, { this.add(AlbumCreator.createAlbum(null, null)) })
            }
        }
    }
}