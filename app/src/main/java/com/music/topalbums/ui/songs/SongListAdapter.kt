package com.music.topalbums.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.utilities.Utilities.formatTimeMinSec
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.SongItemBinding
import com.music.topalbums.utilities.ClickListenerHandler

/**
 * Defines the look of the song recycle view item.
 *
 * @param onSelectedItem = called when the user double clicks or long clicks on an song item
 */
class SongListAdapter(val onSelectedItem:(song: Song) -> Unit):PagingDataAdapter<Song, SongListAdapter.SongListViewHolder>(DiffCallback)
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
            binding.posTextView.text = "${position+1}"
            binding.nameTextView.text = "${song.trackName}"
            binding.durationTextView.text = composeDurationText(song.trackTimeMillis!!)

            //make this item double and long clickable
            ClickListenerHandler(binding.root, onSelectedItem).apply()
            {
               setDoubleClickListener(song)
               setLongClickListener(song)
            }
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