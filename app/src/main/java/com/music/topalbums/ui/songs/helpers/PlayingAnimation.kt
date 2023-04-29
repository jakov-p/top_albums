package com.music.topalbums.ui.songs.helpers

import android.view.View
import com.music.topalbums.R
import com.music.topalbums.databinding.SongItemBinding
import com.music.topalbums.logger.Logger
import com.music.topalbums.ui.songs.SongListAdapter
import com.music.topalbums.utilities.Utilities.loadImage

/***
 * Deals with the animation (gif) on the row of the song that was selected for playing.
 * While playing the gif is shown over the song's duration area, when finished
 * the song's duration area is visible again.
 */
class PlayingAnimation(val songListAdapter: SongListAdapter)
{
    //null means 'no song is being played right now'
    private var currentlyPlayingPos : Int? = null
    val TAG = PlayingAnimation::class.java.simpleName

    /** called when the song playing has finished */
    fun notifyPlayFinished()
    {
        Logger.loggable.i(TAG, "Playing has finished.")

        val oldCurrentlyPlayingPos = currentlyPlayingPos
        currentlyPlayingPos = null

        //force redrawing of only this one row
        songListAdapter.notifyItemChanged(oldCurrentlyPlayingPos!!)
    }

    /**
     * Called for any item (row) in the adapter
     * A chance to update the look to indicate whether this row (song) is being played
     */
    fun show(binding: SongItemBinding, position: Int )
    {
        with(binding)
        {
            val image = requireNotNull(songListAdapter.context.getDrawable(R.drawable.sound_playing))
            songPlayingImageView.loadImage(image)

            songPlayingImageView.visibility = if (currentlyPlayingPos == position) View.VISIBLE else View.GONE
            endTextView.visibility = if (currentlyPlayingPos != position) View.VISIBLE else View.GONE
        }
    }

    /**called when the user clicks (selects) a song for playing */
    fun onSelectedSong(position: Int)
    {
        Logger.loggable.i(TAG, "Playing has started.")

        currentlyPlayingPos = position

        //force redrawing of only this one row
        songListAdapter.notifyItemChanged(position)
    }
}