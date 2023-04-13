package com.music.topalbums.clientapi.utilities

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(context: Context) : Interceptor
{
    private val mContext: Context = context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response
    {
        if (!isConnected)
        {
            throw NoInternetException()
        } else
        {
            val builder: Request.Builder = chain.request().newBuilder()
            return chain.proceed(builder.build())
        }
    }

    private val isConnected: Boolean
        get()
        {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }


    class NoInternetException : IOException("No Internet Connection")
}
