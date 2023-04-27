package com.music.topalbums.data.topalbums


import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.retrofit.ClientApi
import com.music.topalbums.data.albums.topalbums.repository.TopAlbumsRepository
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito

@RunWith(JUnit4::class)
class TopAlbumsRepositoryTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        runBlocking {
            val limit = 20
            val topAlbumsRepository: TopAlbumsRepository = MyTopAlbumsRepository(limit)
            assertEquals("US", topAlbumsRepository.country)
            assertEquals(limit, topAlbumsRepository.limit)
            assertEquals(limit, topAlbumsRepository.getAlbumsCount())
            assertArrayEquals(Utilities.createAlbumCollection(0, 12).list.toTypedArray() , topAlbumsRepository.getAlbums(0, 12).toTypedArray())
            assertArrayEquals(Utilities.createAlbumCollection(10, 12).list.toTypedArray() , topAlbumsRepository.getAlbums(10, 12).toTypedArray())
        }

    }

    open class MyTopAlbumsRepository(limit:Int): TopAlbumsRepository( "US", limit)
    {
        override val clientApi: IClientApi = Mockito.mock(IClientApi::class.java).apply {
            runBlocking {
                Mockito.`when`(getTopAlbums(anyString(), anyInt())).thenReturn(Utilities.createAlbumCollection(0, limit))
            }
        }
    }
}