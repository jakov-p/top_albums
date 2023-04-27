package com.music.topalbums.data.topalbums.datamanager.debug

import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.data.topalbums.Utilities

import com.music.topalbums.data.albums.topalbums.datamanager.debug.SimpleTopAlbumsDataManager
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(JUnit4::class)
class SimpleTopAlbumsDataManagerTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    val limit = 100
    lateinit var simpleTopAlbumsDataManager: SimpleTopAlbumsDataManager

    @Before
    fun setUp()
    {
        simpleTopAlbumsDataManager = MySimpleTopAlbumsDataManager()
    }

    @Test
    fun `when no filter expect all items`()
    {
        runBlocking {
            Assert.assertEquals("US", simpleTopAlbumsDataManager.country)
            Assert.assertEquals(limit, simpleTopAlbumsDataManager.limit)

            Assert.assertEquals(limit, simpleTopAlbumsDataManager.getAlbumsCount())
            Assert.assertArrayEquals(Utilities.createAlbumCollection(0, 5).list.toTypedArray() , simpleTopAlbumsDataManager.getAlbums(0, 5).toTypedArray())
        }
    }

    @Test
    fun `when a matching filter expect filtered items`()
    {
        runBlocking {

            simpleTopAlbumsDataManager.filter = { album ->  album.originalPos?.let { it in 2 until 10 }?: false }

            Assert.assertEquals(8, simpleTopAlbumsDataManager.getAlbumsCount())
            Assert.assertArrayEquals(Utilities.createAlbumCollection(2, 10).list.toTypedArray() , simpleTopAlbumsDataManager.getAlbums(0, 8).toTypedArray())
        }
    }

    @Test
    fun `when no match filter expect no items`()
    {
        runBlocking {

            simpleTopAlbumsDataManager.filter = { album ->  album.artistName?.equals("AAAA")?:false }

            Assert.assertEquals(0, simpleTopAlbumsDataManager.getAlbumsCount())
        }
    }

    open class MySimpleTopAlbumsDataManager: SimpleTopAlbumsDataManager("US")
    {
        override val clientApi: IClientApi = Mockito.mock(IClientApi::class.java).apply {
            runBlocking {
                Mockito.`when`(getTopAlbums(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(Utilities.createAlbumCollection(0, limit))
            }
        }
    }

}