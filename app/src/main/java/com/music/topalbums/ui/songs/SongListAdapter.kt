package com.music.topalbums.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.Utilities.loadImage
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.SongItemBinding

class SongListAdapter:PagingDataAdapter<Song, SongListAdapter.SongListViewHolder>(DiffCallback)
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

    class SongListViewHolder(val binding: SongItemBinding) : ViewHolder(binding.root)
    {
        fun bind(song: Song, position: Int)
        {
            val restText = "price =  ${song.collectionPrice} ${song.currency} \n pos = $position "
            binding.posTextView.text = "${position+1}"
            binding.nameTextView.text = "${song.trackName}"
            binding.durationTextView.text = "${composeDuration(song.trackTimeMillis)}"
        }

        fun bindPlaceHolder()
        {

        }

        private fun composeDuration(durationMs: Int?):String
        {
            return "" + (durationMs?.div(1000) ?: -1)
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