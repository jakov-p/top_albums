package com.music.topalbums.clientapi

import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.collection.AlbumCollection
import com.music.topalbums.clientapi.retrofit.model.AlbumSongsCollection
import com.music.topalbums.clientapi.retrofit.utilities.CallPerformer
import com.music.topalbums.clientapi.retrofit.utilities.LogJsonInterceptor
import com.music.topalbums.clientapi.collection.SongCollection
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.clientapi.retrofit.ServiceApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

//TODO UNFINISHED
@RunWith(MockitoJUnitRunner::class)
class RetrofitServiceTest
{
    private var mockWebServer = MockWebServer()
    private lateinit var serviceApi: IServiceApi

    @Before
    fun setup() {
        mockWebServer.start()

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(LogJsonInterceptor())
            //.addInterceptor(NetworkConnectionInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceApi = retrofit.create(IServiceApi::class.java)
    }

    @After
    fun teardown()
    {
        mockWebServer.shutdown()
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
            val albumSongs: Response<AlbumSongsCollection> = serviceApi.getAlbumSongs(101)

            val runner = CallPerformer("Get Songs On Album Request"){ albumSongs }
            val albumSongsCollection: AlbumSongsCollection? = runner.perform()
            val songCollection: SongCollection = SongCollection(albumSongsCollection!!)

            println(songCollection)
        }
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
            val topAlbums: Response<TopAlbumsCollection> = serviceApi.getTopAlbums("us", 8)

            val runner = CallPerformer("Get Top Albums Request"){ topAlbums }
            val topAlbumsCollection: TopAlbumsCollection? = runner.perform()
            val albumCollection = topAlbumsCollection?.let { AlbumCollection(it) }

            println(albumCollection)
        }
    }

    private fun readFromFile(name:String ) =  File(javaClass.getResource(name).path).readText()
}
