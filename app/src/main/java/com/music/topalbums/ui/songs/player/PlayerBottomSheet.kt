package com.music.topalbums.ui.songs.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.music.topalbums.R
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.BottomSheetPlayerBinding
import com.music.topalbums.utilities.Utilities.showLongToastMessage
import com.music.topalbums.logger.Logger.loggable
import com.music.topalbums.utilities.Utilities.showShortToastMessage

/**
 * GUI offering the user buttons for play, stop and pause functionalities.
 * It also contains a seekbar informing about the song playing progress.
 *
 * @param song song to be played
 */
class PlayerBottomSheet(val song:Song) : BottomSheetDialogFragment()
{
    val TAG = PlayerBottomSheet::class.java.simpleName

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
        with(binding)
        {
            setPlayerEventListener()
            initializeButtons()
            initializeSeekBar()

            //start playing the song immediately when the GUI appears on the screen
            playButton.callOnClick()
        }
    }

    private fun BottomSheetPlayerBinding.setPlayerEventListener()
    {
        playerWrapper.eventListener = object : PlayerWrapper.IPlayerListener
        {
            override fun onPlay()
            {
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                //showShortToastMessage("Media playing.")
            }

            override fun onPause()
            {
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = true
                //showShortToastMessage("Media paused.")
            }

            override fun onStop()
            {
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = false
                leftSideTextView.text = ""
                rightSideTextView.text = ""
                //showShortToastMessage("Media stopped.")
            }

            override fun onProgress(currentPos: Int)
            {
                val durationInSec = playerWrapper.duration / 1000
                val currentPosInSec = currentPos / 1000

                //adapt the seekbar and text over the seekbar to show the current playing position
                seekBar.progress = currentPosInSec
                leftSideTextView.text = song.trackName
                rightSideTextView.text = "${Utilities.formatTimeMinSec(currentPosInSec)} / ${Utilities.formatTimeMinSec(durationInSec)}"
            }
        }
    }

    /**
     * Handles actions performed by the user on the seekbar
     * (when the user jumps to a certain second of the song)
     */
    private fun BottomSheetPlayerBinding.initializeSeekBar()
    {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean)
            {
                if (b)
                {
                    playerWrapper.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar){} //no need
            override fun onStopTrackingTouch(seekBar: SeekBar){} //no need
        })
    }


    /** Connects the buttons to the corresponding commands of media player wrapper */
    private fun BottomSheetPlayerBinding.initializeButtons()
    {
        playButton.setOnClickListener {
            playerWrapper.play()
            seekBar.max = playerWrapper.duration / 1000
        }

        pauseButton.setOnClickListener {
            playerWrapper.pause()
        }

        stopButton.setOnClickListener {
            playerWrapper.stop()
            seekBar.setProgress(0)
        }
    }


    /** Called on any unrecoverable error fired from the media player wrapper */
    private fun onPlayerError()
    {
        try
        {
            loggable.e(TAG, "An error occurred. Player can not play the song.")
            showLongToastMessage(requireContext(), requireContext().getString(R.string.player_can_not_play))
            dismiss() //close the GUI
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
