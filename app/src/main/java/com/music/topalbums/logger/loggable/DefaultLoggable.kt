package com.music.topalbums.logger.loggable

import android.util.Log


/**
 * Logs by using the standard Android logger.
 */
class SimpleNameDefaultLoggable( ): Loggable
{
    override fun i(tag: String, message: String)
    {
        Log.i(tag, message)
    }

    override fun d(tag: String, message: String)
    {
        Log.d(tag , message)
    }

    override fun w(tag: String, message: String)
    {
        Log.w(tag , message)
    }

    override fun e(tag: String, message: String)
    {
        Log.e(tag , message)
    }

    override fun e(tag: String, message: String, ex:Exception)
    {
        Log.e(tag  , message)
    }

}

