package com.music.topalbums.data.songs


import com.music.topalbums.clientapi.IClientApi
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
class SongsRepositoryTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        runBlocking {
            val collectionId = 10001
            val size = 20

            val songsRepository: SongsRepository = MySongsRepository(collectionId, size)
            assertEquals(collectionId, songsRepository.collectionId)
            assertEquals(size, songsRepository.getSongsCount())
            assertArrayEquals(Utilities.createSongCollection(0, 12).list.toTypedArray() , songsRepository.getSongs(0, 12).toTypedArray())
            assertArrayEquals(Utilities.createSongCollection(10, 12).list.toTypedArray() , songsRepository.getSongs(10, 12).toTypedArray())
        }

    }

    open class MySongsRepository(collectionId: Int, val size:Int ): SongsRepository(collectionId)
    {
        override val clientApi: IClientApi = Mockito.mock(IClientApi::class.java).apply {
            runBlocking {
                Mockito.`when`(getAlbumSongs(collectionId)).thenReturn(Utilities.createSongCollection(0, size))
            }
        }
    }
}