package com.music.topalbums.ui.topalbums.albums_paging_source

import androidx.paging.PagingSource
import com.music.topalbums.AlbumCreator
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManager
import com.music.topalbums.ui.topalbums.AlbumsPagingSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.mockito.Mockito

class ScenarioPerformer
{
    val topAlbumsDataManager = Mockito.mock(ITopAlbumsDataManager::class.java)
    val albumsPagingSource = AlbumsPagingSource(topAlbumsDataManager)

    fun runSingle(block: suspend ScenarioPerformer.() -> Unit, loadParams: PagingSource.LoadParams<Int>, expectedLoadResult: PagingSource.LoadResult.Page<Int, Album>)
    {
        runBlocking {
            Mockito.reset(topAlbumsDataManager)
            block()

            val loadResult: PagingSource.LoadResult<Int, Album> = this@ScenarioPerformer.albumsPagingSource.load(loadParams)

            if(loadResult is PagingSource.LoadResult.Page)
            {
                compareResult(expectedLoadResult, loadResult)
            }
            else
            {
                Assert.fail("'loadResult' is not of type 'PagingSource.LoadResult.Page', but should be. 'loadResult' = $loadResult")
            }
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
        Assert.assertEquals(expected.data.size, actual.data.size)
        Assert.assertEquals(expected.prevKey, actual.prevKey)
        Assert.assertEquals(expected.nextKey, actual.nextKey)
        Assert.assertEquals(expected.itemsBefore, actual.itemsBefore)
        Assert.assertEquals(expected.itemsAfter, actual.itemsAfter)
    }

    suspend fun addAlbumsCount(isForFullLoad: Boolean, result: Int) =
        Mockito.`when`(topAlbumsDataManager.getAlbumsCount(isForFullLoad)).thenReturn(result)

    suspend fun addAlbums(isForFullLoad: Boolean, fromIndex: Int, toIndex: Int, resultListSize: Int) =
        Mockito.`when`(topAlbumsDataManager.getAlbums(isForFullLoad, fromIndex, toIndex)).thenReturn(createList(resultListSize))

    fun addIsFullyLoaded(result: Boolean) =
        Mockito.`when`(topAlbumsDataManager.isFullyLoaded).thenReturn(result)


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