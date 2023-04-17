package com.music.topalbums.ui.songs

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.R
import com.music.topalbums.data.albums.Album
import com.music.topalbums.utilities.Utilities.formatTimeMinSec
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.SongItemBinding
import com.music.topalbums.utilities.ClickListenerHandler

/**
 * Defines the look of the song recycle view item.
 *
 * @param onSelectedItem = called when the user double clicks or long clicks on an song item
 */
class SongListAdapter(val context: Context, val onSelectedItem:(song: Song) -> Unit):PagingDataAdapter<Song, SongListAdapter.SongListViewHolder>(DiffCallback)
{
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

    inner class SongListViewHolder(val binding: SongItemBinding) : ViewHolder(binding.root)
    {
        fun bind(song: Song, position: Int)
        {
            val restText = "price =  ${song.collectionPrice} ${song.currency} \n pos = $position "
            binding.startTextView.text = "${position+1}.".padStart(3, '0') //e.g. '05.', '15.'
            binding.midTextView.text = "${song.trackName}"
            binding.endTextView.text = composeDurationText(song.trackTimeMillis!!)

            //make this item double and long clickable
            ClickListenerHandler(binding.root,::onSelectedSong).
                apply {
                   setDoubleClickListener(song)
                   setLongClickListener(song)
                }
        }

        fun onSelectedSong(song: Song)
        {
            //flash for a moment with a different color
            val oldBackground = binding.root.background
            binding.root.setBackgroundColor(context.getColor(R.color.item_selection))
            Handler(context.mainLooper).postDelayed( {
                binding.root.background = oldBackground
            }, 200)

            onSelectedItem(song)
        }

        fun bindPlaceHolder()
        {
            //nothing here to be done
        }

        private fun composeDurationText(durationMs: Int?):String
        {
            return if(durationMs!=null)
            {
                // e.g. 309 000 --> "05:09"
                formatTimeMinSec(durationMs/ 1000)
            }
            else
            {
                "-:-" //unknow duration
            }
        }

    }



    fun isEmpty() = (itemCount == 0)

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