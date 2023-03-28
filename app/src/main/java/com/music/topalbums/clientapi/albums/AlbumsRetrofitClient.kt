package com.music.topalbums.clientapi.albums

import com.music.topalbums.clientapi.LogJsonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//https://itunes.apple.com/us/rss/topalbums/limit=100/json`
class AlbumsRetrofitClient
{
    val serverUrl: String = "https://itunes.apple.com"
    val service: IAlbumsApi

    init
    {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(LogJsonInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(IAlbumsApi::class.java)
    }
}

