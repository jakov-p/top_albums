package com.music.topalbums.data.songs


import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.collection.Album
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
class SongsDataManagerTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    @Test
    fun test()
    {
        runBlocking {
            val size = 20

            val album = com.music.topalbums.data.topalbums.Utilities.createAlbum(0)

            val songsDataManager: SongsDataManager = MySongsDataManager(album, size)
            assertEquals(album.collectionId, songsDataManager.collectionId)
            assertEquals(size, songsDataManager.getSongsCount())
            assertArrayEquals(Utilities.createSongCollection(0, 12).list.toTypedArray() , songsDataManager.getSongs(0, 12).toTypedArray())
            assertArrayEquals(Utilities.createSongCollection(10, 12).list.toTypedArray() , songsDataManager.getSongs(10, 12).toTypedArray())
        }

    }

    open class MySongsDataManager(album: Album, size:Int): SongsDataManager(album)
    {
        override val clientApi: IClientApi = Mockito.mock(IClientApi::class.java).apply {
            runBlocking {
                Mockito.`when`(getAlbumSongs(collectionId)).thenReturn(Utilities.createSongCollection(0, size))
            }
        }
    }
}