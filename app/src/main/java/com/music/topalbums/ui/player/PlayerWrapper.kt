package com.music.topalbums.ui.player

import android.media.MediaPlayer
import android.os.Handler

class PlayerWrapper(var audioUrl:String)
{
    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false
    private var isStopped: Boolean = false
    var eventListener : IPlayerListener? = null

    var progressInterval:Long = 500
    private val handler: Handler = Handler()
    private lateinit var runnable: Runnable

    fun play()
    {
        if (isPaused)
        {
            mediaPlayer.seekTo(mediaPlayer.currentPosition)
            isPaused = false
        }
        else
        {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepare()
            mediaPlayer.setVolume(0.5f, 0.5f)
            mediaPlayer.isLooping = false
        }
        startProgressEvents()

        eventListener?.onPlay()
        mediaPlayer.start()
        isStopped = false;

        mediaPlayer.setOnCompletionListener {
            eventListener?.onStop()
        }
    }

    fun pause()
    {
        if (mediaPlayer.isPlaying)
        {
            mediaPlayer.pause()
            isPaused = true
            isStopped = false;

            eventListener?.onPause()
        }
    }

    fun stop()
    {
        if (mediaPlayer.isPlaying || isPaused)
        {
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            isStopped = true;

            handler.removeCallbacks(runnable)

            eventListener?.onStop()
        }
    }

    fun destroy()
    {
        if(::mediaPlayer.isInitialized)
        {
            if (!isStopped)
            {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
            }
        }

        if(::runnable.isInitialized)
        {
            handler.removeCallbacks(runnable)
        }
    }


    // Method to initialize seek bar and audio stats
    private fun startProgressEvents()
    {
        runnable = Runnable {
            eventListener?.onProgress(mediaPlayer.currentPosition)
            handler.postDelayed(runnable, progressInterval)
        }
        handler.postDelayed(runnable, progressInterval)
    }

    fun seekTo(toMilisecond: Int) = mediaPlayer.seekTo(toMilisecond)

    val duration: Int
        get() { return mediaPlayer.duration }

    //get media player current position in seconds
    val currentPosition: Int
        get() { return  mediaPlayer.currentPosition}


    interface IPlayerListener
    {
        fun onPlay()
        fun onPause()
        fun onStop()
        fun onProgress(currentPos:Int)
    }
}