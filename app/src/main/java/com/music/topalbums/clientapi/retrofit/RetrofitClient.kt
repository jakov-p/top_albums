package com.music.topalbums.clientapi.retrofit

import com.music.topalbums.clientapi.retrofit.utilities.LogJsonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Qualifier

class RetrofitClient @Inject constructor(@ServerUrl serverUrl: String)
{
    companion object {
        val SERVER_URL: String = "https://itunes.apple.com"
    }

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



@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerUrl

