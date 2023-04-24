package com.music.topalbums.logger

import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.logger.loggable.Loggable
import com.music.topalbums.logger.loggable.SimpleNameDefaultLoggable
import com.music.topalbums.logger.loggable.TestLoggable
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.android.EarlyEntryPoints
import dagger.hilt.components.SingletonComponent

object Logger
{
    var loggable: Loggable = TestLoggable()

    //var loggable: Loggable = SimpleNameDefaultLoggable()
    //val loggable: Loggable = EarlyEntryPoints.get(TopAlbumsApp.appContext, ILoggerEntryPoint::class.java).getLoggable()

    fun printTitle(tag: String, message:String)
    {
        loggable.d(tag," ")
        loggable.d(tag,  "***************************************************************************************************")
        loggable.d(tag,  "*                                                                                                 *")
        loggable.d(tag,  "*                             " + message + "")
        loggable.d(tag,  "*                                                                                                 *")
        loggable.d(tag,  "***************************************************************************************************")
        loggable.d(tag,"")
    }

    fun printSubtitle(tag: String, message: String)
    {
        loggable.i(tag, "****************************************************************************** ")
        loggable.i(tag, "                       $message ...")
        loggable.i(tag, "****************************************************************************** ")
    }


    @EarlyEntryPoint
    @InstallIn(SingletonComponent::class)
    interface ILoggerEntryPoint
    {
        fun getLoggable(): Loggable
    }

}
