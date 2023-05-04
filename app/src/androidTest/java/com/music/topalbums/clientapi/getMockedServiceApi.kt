package com.music.topalbums.clientapi

import android.content.res.AssetManager
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.clientapi.retrofit.RetrofitClient
import com.music.topalbums.clientapi.retrofit.ServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection

/**
 * @property jsonText represents text in json format as if downloaded from the server
 */
class MockedServiceApi(val jsonText:String)
{
    fun  getServiceApi(): IServiceApi
    {
        return runBlocking {
            withContext(Dispatchers.IO){

                val mockWebServer = MockWebServer()
                mockWebServer.start()
                val serverUrl = mockWebServer.url("/").toString()
                val response = MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(jsonText)

                mockWebServer.enqueue(response)

                ServiceApi(RetrofitClient(serverUrl))
            }
        }
    }

    companion object
    {
        fun readFromAssets(fileName: String): String
        {
            val testContext = InstrumentationRegistry.getInstrumentation().getContext()
            val assetManager: AssetManager = testContext.getAssets()
            val inputStream: InputStream = assetManager.open(fileName)
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }
}