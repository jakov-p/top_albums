package com.music.topalbums.ui.songs

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Song
import com.music.topalbums.databinding.SongItemBinding
import com.music.topalbums.ui.songs.helpers.PlayingAnimation
import com.music.topalbums.utilities.ClickListenerHandler
import com.music.topalbums.utilities.Utilities

/**
 * Defines the look of the song recycle view item.
 *
 * @param onSelectedItem = called when the user double clicks or long clicks on an song item
 */
class SongListAdapter(val context: Context, val onSelectedItem:(song: Song) -> Unit):PagingDataAdapter<Song, SongListAdapter.SongListViewHolder>(DiffCallback)
{
    val TAG = SongListAdapter::class.java.simpleName

    val playingAnimation =  PlayingAnimation(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder
    {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int)
    {
        getItem(position)?.let {
            holder.bind(it, position) }
        ?:
            holder.bindPlaceHolder()
    }

    fun notifyPlayFinished()
    {
        playingAnimation.notifyPlayFinished()
    }

    inner class SongListViewHolder(val binding: SongItemBinding) : ViewHolder(binding.root)
    {
        fun bind(song: Song, position: Int)
        {
            with(binding)
            {
                //e.g.  '01.   SongName    03:18'
                startTextView.text = "${position + 1}.".padStart(3, '0') //e.g. '05.', '15.'
                midTextView.text = "${song.trackName}"
                endTextView.text = Utilities.composeDurationText(song.trackTimeMillis)

                //make this item double and long clickable
                ClickListenerHandler(root, ::onSelectedSong).apply {
                    setClickListener(song, position)
                }

                playingAnimation.show(binding, position)
                setAlternateColor(position)
            }
        }

        fun onSelectedSong(song: Song, position: Int)
        {
            //flash for a moment with a different color
            binding.root.setBackgroundColor(context.getColor(R.color.item_selection))
            Handler(context.mainLooper).postDelayed({
                setAlternateColor(position)
            }, 200)

            //send the event
            onSelectedItem(song)

            playingAnimation.onSelectedSong(position)
        }


        /**
         *  Update the background color according to the odd/even positions in the list.
         *  @param position position in the list
         */
        private fun setAlternateColor(position: Int)
        {
            val color = if (position % 2 == 0) context.getColor(R.color.item_normal)else context.getColor(R.color.item_normal_alternate)
            binding.root.setBackgroundColor( color)
        }

        fun bindPlaceHolder()
        {
            //nothing here to be done
        }

    }

    object DiffCallback: DiffUtil.ItemCallback<Song>()
    {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean
        {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean
        {
            return oldItem == newItem
        }
    }



}