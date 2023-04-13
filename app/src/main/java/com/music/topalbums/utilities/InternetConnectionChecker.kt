package com.music.topalbums.utilities

import android.content.Context
import android.net.ConnectivityManager

class InternetConnectionChecker(val context:Context): IInternetConnectionChecker
{
    override val isConnected: Boolean
        get()
        {
            val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
}

interface IInternetConnectionChecker
{
    val isConnected: Boolean
}