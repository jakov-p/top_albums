package com.music.topalbums.data.topalbums

import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(JUnit4::class)
class FilteredTopAlbumsRepositoryTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    val limit = 20
    lateinit var filteredRepository: FilteredTopAlbumsRepository

    @Before
    fun setUp()
    {
        filteredRepository = MyFilteredTopAlbumsRepository(limit)
    }

    @Test
    fun `when no filter expect all items`()
    {
        runBlocking {
            Assert.assertEquals("US", filteredRepository.country)
            Assert.assertEquals(limit, filteredRepository.limit)

            Assert.assertEquals(limit, filteredRepository.getAlbumsCount())
            Assert.assertArrayEquals(Utilities.createAlbumCollection(0, 5).list.toTypedArray() , filteredRepository.getAlbums(0, 5).toTypedArray())
        }
    }

    @Test
    fun `when a matching filter expect filtered items`()
    {
        runBlocking {

            filteredRepository.filter = {album ->  album.originalPos?.let { it in 2 until 10 }?: false }

            Assert.assertEquals(8, filteredRepository.getAlbumsCount())
            Assert.assertArrayEquals(Utilities.createAlbumCollection(2, 10).list.toTypedArray() , filteredRepository.getAlbums(0, 8).toTypedArray())
        }
    }

    @Test
    fun `when no match filter expect no items`()
    {
        runBlocking {

            filteredRepository.filter = {album ->  album.artistName?.equals("AAAA")?:false }

            Assert.assertEquals(0, filteredRepository.getAlbumsCount())
        }
    }

    open class MyFilteredTopAlbumsRepository(limit:Int): FilteredTopAlbumsRepository( "US", limit)
    {
        override val clientApi: IClientApi = Mockito.mock(IClientApi::class.java).apply {
            runBlocking {
                Mockito.`when`(getTopAlbums(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(Utilities.createAlbumCollection(0, limit))
            }
        }
    }
}