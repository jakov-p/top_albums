package com.music.topalbums.clientapi

import com.music.topalbums.clientapi.collection.SongCollection
import com.music.topalbums.clientapi.retrofit.ClientApi
import com.music.topalbums.clientapi.retrofit.RetrofitClient
import com.music.topalbums.clientapi.retrofit.ServiceApi
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.loggable.TestLoggable
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.net.HttpURLConnection

//TODO UNFINISHED
@RunWith(MockitoJUnitRunner::class)
class RetrofitServiceTest
{
    init{
        Logger.loggable = TestLoggable()
    }

    private var mockWebServer = MockWebServer()
    private lateinit var clientApi: IClientApi

    @Before
    fun setup() {

        mockWebServer.start()
        val serverUrl = mockWebServer.url("/").toString()
        clientApi = ClientApi(ServiceApi(RetrofitClient(serverUrl)))
    }

    @After
    fun teardown()
    {
        mockWebServer.shutdown()
    }


    @Test
    fun testTopAlbumsFetching()
    {
        runBlocking {
            // Assign
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(readFromFile("topalbums.json"))

            mockWebServer.enqueue(response)// Act

            val albumCollection = clientApi.getTopAlbums("us", 8)

            println(albumCollection)
            assertEquals(8, albumCollection?.list?.size)
            albumCollection?.list?.let {
                with(it.get(0))
                {
                    assertEquals( "Metallica", artistName)
                    assertEquals( 1655432387, collectionId)
                    assertEquals( "72 Seasons - Metallica", collectionName)
                    assertEquals( 11.99f, collectionPrice)
                    assertEquals( "USD", currency)
                    assertEquals( "Heavy Metal", primaryGenreName)
                    assertEquals( 1153, primaryGenreId)
                    assertEquals( "2023-04-14T00:00:00-07:00", releaseDate)
                }
            }
        }
    }

    @Test
    fun testSongsFetching()
    {
        runBlocking {
            // Assign
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(readFromFile("songs.json"))

            mockWebServer.enqueue(response)// Act

            val songCollection: SongCollection? =  clientApi.getAlbumSongs(101)

            println(songCollection)
            assertEquals(10, songCollection?.list?.size)

            songCollection?.list?.let {
                with(it.get(0))
                {
                    assertEquals( "Everything But the Girl", artistName)
                    assertEquals( 1659426282, collectionId)
                    assertEquals( "Fuse", collectionName)
                    assertEquals( 9.99f, collectionPrice)
                    assertEquals( "USD", currency)
                    assertEquals( "Pop", primaryGenreName)
                    assertEquals( "2023-02-22T12:00:00Z", releaseDate)
                    assertEquals( null, amgArtistId)
                    assertEquals( 164449, artistId)
                    assertEquals( 10, trackCount)
                }
            }
        }
    }


    private fun readFromFile(name:String ) =  File(javaClass.getResource(name).path).readText()
}
