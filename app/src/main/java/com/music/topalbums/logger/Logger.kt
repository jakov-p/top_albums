package com.music.topalbums.logger

import com.music.topalbums.logger.loggable.Loggable
import com.music.topalbums.logger.loggable.SimpleNameDefaultLoggable

object Logger
{
    var loggable: Loggable = SimpleNameDefaultLoggable()

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

}
