package com.music.topalbums.logger.loggable

interface Loggable
{
    fun i(tag: String, message: String)
    fun d(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
    fun e(tag: String, message: String, ex:Exception)

    fun getSimpleName(fullName:String):String
    {
        return fullName.substringAfterLast(".")
    }
}