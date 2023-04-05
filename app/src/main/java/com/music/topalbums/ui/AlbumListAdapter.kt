package com.music.topalbums.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.Utilities.loadImage
import com.music.topalbums.clientapi.albums.data.BasicAlbumsRepository
import com.music.topalbums.databinding.AlbumItemBinding

class AlbumListAdapter:PagingDataAdapter<BasicAlbumsRepository.AlbumWithSongs, AlbumListAdapter.AlbumListViewHolder >(DiffCallback)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListViewHolder
    {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumListViewHolder, position: Int)
    {
        getItem(position)?.let {
            holder.bind(it, position) }
        ?:
            holder.bindPlaceHolder()
    }

    class AlbumListViewHolder(val binding: AlbumItemBinding) : ViewHolder(binding.root)
    {
        fun bind( albumWithSongs: BasicAlbumsRepository.AlbumWithSongs, position: Int)
        {
            val restText = "price =  ${albumWithSongs.album.collectionPrice} ${albumWithSongs.album.currency} \n pos = $position "
            binding.restTextView.text = restText
            binding.nameTextView.text = "${albumWithSongs.album.collectionName}"
            binding.albumCoverImageView.loadImage(albumWithSongs.album.artworkUrl!!)
        }

        fun bindPlaceHolder()
        {

        }

    }


    fun isEmpty() = (itemCount == 0)

    object DiffCallback: DiffUtil.ItemCallback<BasicAlbumsRepository.AlbumWithSongs>()
    {
        override fun areItemsTheSame(oldItem: BasicAlbumsRepository.AlbumWithSongs, newItem: BasicAlbumsRepository.AlbumWithSongs): Boolean
        {
            return oldItem.album.collectionId == newItem.album.collectionId
        }

        override fun areContentsTheSame(oldItem: BasicAlbumsRepository.AlbumWithSongs, newItem: BasicAlbumsRepository.AlbumWithSongs): Boolean
        {
            return oldItem == newItem
        }

    }

}