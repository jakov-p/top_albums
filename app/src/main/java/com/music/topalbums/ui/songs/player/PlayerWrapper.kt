package com.music.topalbums.ui.songs.player

import android.media.MediaPlayer
import android.os.Handler
import com.music.topalbums.logger.Logger.loggable

/**
 * A wrapper around a media player object implementing play, pause,
 * and stop functionality. It also notifies about the playing progress.
 * It has no GUI, just a media player object.
 *
 * TODO
 * Currently loss of connection is not handled well. The 'prepare' command is where
 * media player downloads a song from internet and it is executed in the Main thread.
 * The command 'prepareAsync()' should be called instead.
 *
 * @param audioUrl a song to be played
 * @param onError informs about any (non recoverable) error
 */
class PlayerWrapper(var audioUrl:String, val onError: ()-> Unit)
{
    val TAG = PlayerWrapper::class.java.simpleName

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false //is the media player currently paused?
    private var isStopped: Boolean = false //is the media player currently stopped?

    var eventListener : IPlayerListener? = null

    //how often to send event about the current playing time ('onProgress' event)
    var progressInterval:Long = 500

    //handles sending 'onProgress' events
    private val progressEventsSender: Handler = Handler()
    private lateinit var runnable: Runnable

    fun play() = runInsideTry()
    {
        if (isPaused)
        {
            mediaPlayer.seekTo(mediaPlayer.currentPosition)
            isPaused = false
        }
        else
        {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setOnErrorListener { _, _, _ ->
                onError()
                true
            }

            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepare()
            mediaPlayer.setVolume(0.5f, 0.5f)
            mediaPlayer.isLooping = false //play just once
        }
        startFiringProgressEvents()

        eventListener?.onPlay() //inform
        mediaPlayer.start()
        isStopped = false;

        mediaPlayer.setOnCompletionListener {
            eventListener?.onStop() //inform
        }
    }


    fun pause() = runInsideTry()
    {
        if (mediaPlayer.isPlaying)
        {
            mediaPlayer.pause()
            isPaused = true
            isStopped = false;

            eventListener?.onPause() //inform
        }
    }


    fun stop() = runInsideTry()
    {
        if (mediaPlayer.isPlaying || isPaused)
        {
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            isStopped = true;

            progressEventsSender.removeCallbacks(runnable)

            eventListener?.onStop() //inform
        }
    }

    fun destroy() = runInsideTry()
    {
        if (::mediaPlayer.isInitialized)
        {
            if (!isStopped)
            {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
            }
        }

        if (::runnable.isInitialized)
        {
            progressEventsSender.removeCallbacks(runnable)
        }
    }

    /**
     * Runs a block of code and handles an exception if thrown
     */
    private fun runInsideTry(block:()->Unit)
    {
        try
        {
            block.invoke()
        }
        catch(ex:Exception)
        {
            loggable.e(TAG, "Player exception ", ex)
            onError.invoke()
        }
    }


    /***
     *starts looping to periodically inform about the current playing time
     */
    private fun startFiringProgressEvents()
    {
        runnable = Runnable {
            eventListener?.onProgress(mediaPlayer.currentPosition)
            progressEventsSender.postDelayed(runnable, progressInterval)
        }
        progressEventsSender.postDelayed(runnable, progressInterval)
    }

    /** Forces the player to jump to a particular millisecond of the song */
    fun seekTo(toMilisecond: Int) = mediaPlayer.seekTo(toMilisecond)

    /** The total duration of the song */
    val duration: Int
        get() { return mediaPlayer.duration }

    /** Get media player current position in seconds */
    val currentPosition: Int
        get() { return  mediaPlayer.currentPosition}


    //Informs the GUI (anybody interested) about any change in the media player's state
    interface IPlayerListener
    {
        fun onPlay()
        fun onPause()
        fun onStop()
        fun onProgress(currentPos:Int)
    }
}