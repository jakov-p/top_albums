package com.music.topalbums.ui.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.Utilities.formatTimeMinSec
import com.music.topalbums.Utilities.loadImage
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.SongItemBinding
import com.music.topalbums.ui.player.PlayerBottomSheet
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener

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
            binding.durationTextView.text = composeDuration(song.trackTimeMillis!!)

            setDoubleClickListener(song)
        }

        private fun setDoubleClickListener(song: Song)
        {
            val doubleClick = DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    // DO STUFF SINGLE CLICK
                }

                override fun onDoubleClick(view: View) {
                    onSelectedItem(song)
                }
            })

            with(binding)
            {
                root.setOnClickListener(doubleClick)
                root.allViews.forEach {
                    it.setOnClickListener(doubleClick)
                }
            }
        }

        fun bindPlaceHolder()
        {

        }

        private fun composeDuration(durationMs: Int?):String
        {
            return if(durationMs!=null)
            {
                formatTimeMinSec(durationMs/ 1000)
            }
            else
            {
                "-:-"
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