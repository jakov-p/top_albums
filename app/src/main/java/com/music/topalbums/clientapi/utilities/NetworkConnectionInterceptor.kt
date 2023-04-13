package com.music.topalbums.clientapi.utilities

import android.content.Context
import android.net.ConnectivityManager
import com.music.topalbums.utilities.IInternetConnectionChecker
import com.music.topalbums.utilities.InternetConnectionChecker
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor() : Interceptor
{
    lateinit var internetConnectionChecker: IInternetConnectionChecker

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response
    {
        if (!internetConnectionChecker.isConnected)
        {
            throw NoInternetException()
        }
        else
        {
            val builder: Request.Builder = chain.request().newBuilder()
            return chain.proceed(builder.build())
        }
    }


    class NoInternetException : IOException("No Internet Connection")
}
