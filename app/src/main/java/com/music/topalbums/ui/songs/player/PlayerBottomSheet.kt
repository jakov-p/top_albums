package com.music.topalbums.ui.songs.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.BottomSheetPlayerBinding
import com.music.topalbums.utilities.Utilities.showToastMessage


class PlayerBottomSheet(val song:Song) : BottomSheetDialogFragment()
{
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var leftSideTextView: TextView
    private lateinit var rightSideTextView: TextView

    private lateinit var binding: BottomSheetPlayerBinding

    private val playerWrapper: PlayerWrapper = PlayerWrapper(song.previewUrl!!, ::onPlayerError)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = BottomSheetPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init()
    {
        playButton = binding.playButton
        pauseButton = binding.pauseButton
        stopButton = binding.stopButton
        seekBar = binding.seekBar
        leftSideTextView = binding.leftSideTextView
        rightSideTextView = binding.rightSideTextView

        playerWrapper.eventListener = object: PlayerWrapper.IPlayerListener
        {
            override fun onPlay()
            {
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                showToastMessage("media playing")
            }

            override fun onPause()
            {
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = true
                showToastMessage("media pause")
            }

            override fun onStop()
            {
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = false
                leftSideTextView.text = ""
                rightSideTextView.text = ""
                showToastMessage( "media stop")
            }

            override fun onProgress(currentPos: Int)
            {
                val durationInSec = playerWrapper.duration / 1000
                val currentPosInSec = currentPos / 1000

                seekBar.progress = currentPosInSec
                leftSideTextView.text = song.trackName
                rightSideTextView.text = Utilities.formatTimeMinSec(currentPosInSec) + " / " + Utilities.formatTimeMinSec(durationInSec)
            }
        }

        // Start the media player
        playButton.setOnClickListener {
            playerWrapper.play()
            seekBar.max = playerWrapper.duration / 1000
        }

        // Pause the media player
        pauseButton.setOnClickListener {
            playerWrapper.pause()

        }
        // Stop the media player
        stopButton.setOnClickListener {
            playerWrapper.stop()
            seekBar.setProgress(0)
        }

        // Seek bar change listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean)
            {
                if (b)
                {
                    playerWrapper.seekTo(i*1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
            }
        })

        playButton.callOnClick()
    }

    private fun onPlayerError()
    {
        try
        {
            println("An error occurred. Player can not play the song.")
            showToastMessage(requireContext(), "An error occurred. Player can not play the song.") //TODO resource
            dismiss();
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }
    }

    override fun onPause()
    {
        super.onPause()
        dismiss();
    }

    override fun onDestroy()
    {
        super.onDestroy()
        playerWrapper.destroy()
    }

}
