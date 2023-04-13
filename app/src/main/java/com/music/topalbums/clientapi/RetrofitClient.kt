package com.music.topalbums.clientapi

import com.music.topalbums.clientapi.utilities.LogJsonInterceptor
import com.music.topalbums.clientapi.utilities.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient
{
    val serverUrl: String = "https://itunes.apple.com"
    val service: IServiceApi

    init
    {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(LogJsonInterceptor())
            //.addInterceptor(NetworkConnectionInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(IServiceApi::class.java)
    }
}

