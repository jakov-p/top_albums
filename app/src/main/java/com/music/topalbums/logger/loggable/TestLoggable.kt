package com.music.topalbums.logger.loggable


/***
 * Good for unit tests, because it does not use Android's logger system at all.
 */
object TestLoggable: Loggable
{
    override fun i(tag: String, message: String)
    {
        print(tag + ":" + message)
    }

    override fun d(tag: String, message: String)
    {
        print(tag + ":" + message)
    }

    override fun w(tag: String, message: String)
    {
        print(tag + ":" + message)
    }

    override fun e(tag: String, message: String)
    {
        print(tag + ":" + message)
    }

    override fun e(tag: String, message: String, ex: Exception)
    {
        print(tag + ":" + message)
        ex.printStackTrace()
    }
}

