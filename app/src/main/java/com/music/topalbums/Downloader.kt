package com.music.topalbums

import com.music.topalbums.clientapi.albums.AlbumsRepository
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.Logger.loggable
import kotlinx.coroutines.*

object Downloader
{
    val TAG = Downloader::class.java.simpleName
    lateinit var coroutineScope: CoroutineScope //lives only for a duration of a single session

    private var isExternallyCanceled: Boolean = false

    fun download(onSuccess: () -> Unit, onFailure: ( ) -> Unit )
    {
        isExternallyCanceled = false

        startCoroutine {
            try
            {
                val topAlbums = AlbumsRepository.getTopAlbums("us", 10)
                //topAlbums is currently full if nulls
                onSuccess()
            }
            catch (exception: CancellationException)
            {
                if (isExternallyCanceled)
                {
                    Logger.loggable.d(TAG, "Canceled externally, " + exception.message)
                    onFailure()
                }
                else
                {
                    Logger.loggable.w(TAG, "Canceled ?????" + exception.message)
                    onFailure()
                }
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
                Logger.loggable.e(TAG, exception.toString(), exception)
                onFailure()
            }
        }
    }

    fun cancel()
    {
        loggable.d(TAG, "Cancelling externally...  ")
        isExternallyCanceled = true
        coroutineScope.cancel()
    }


    private fun startCoroutine(block: suspend CoroutineScope.() -> Unit )
    {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }

        //each session has its own coroutine scope
        coroutineScope = CoroutineScope(Dispatchers.IO + exceptionHandler)
        coroutineScope.launch {
            block.invoke(this)
        }
    }
}