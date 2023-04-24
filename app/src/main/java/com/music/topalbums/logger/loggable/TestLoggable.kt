package com.music.topalbums.logger.loggable

import javax.inject.Inject


/***
 * Good for unit tests, because it does not use Android's logger system at all.
 */
class TestLoggable @Inject constructor(): Loggable
{
    override fun i(tag: String, message: String)
    {
        println(tag + ":" + message)
    }

    override fun d(tag: String, message: String)
    {
        println(tag + ":" + message)
    }

    override fun w(tag: String, message: String)
    {
        println(tag + ":" + message)
    }

    override fun e(tag: String, message: String)
    {
        println(tag + ":" + message)
    }

    override fun e(tag: String, message: String, ex: Exception)
    {
        println(tag + ":" + message)
        ex.printStackTrace()
    }
}

