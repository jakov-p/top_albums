package com.music.topalbums.ui.topalbums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.utilities.Utilities.loadImage
import com.music.topalbums.data.albums.Album
import com.music.topalbums.databinding.AlbumItemBinding
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener

class AlbumsListAdapter(val onSelectedItem:(album: Album) -> Unit):PagingDataAdapter<Album, AlbumsListAdapter.AlbumListViewHolder>(DiffCallback)
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

    inner class AlbumListViewHolder(val binding: AlbumItemBinding) : ViewHolder(binding.root)
    {
        fun bind(album: Album, position: Int)
        {
            val restText = "price =  ${album.collectionPrice} ${album.currency} \n pos = $position "
            with(binding)
            {
                restTextView.text = restText
                nameTextView.text = "${album.collectionName}"
                albumCoverImageView.loadImage(album.collectionImageUrl!!)

                setDoubleClickListener(album)
            }
        }

        fun setDoubleClickListener(album: Album)
        {
            val doubleClick = DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    // DO STUFF SINGLE CLICK
                }

                override fun onDoubleClick(view: View) {
                    onSelectedItem(album)
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

    }


    fun isEmpty() = (itemCount == 0)

    object DiffCallback: DiffUtil.ItemCallback<Album>()
    {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean
        {
            return oldItem.collectionId == newItem.collectionId
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean
        {
            return oldItem == newItem
        }

    }

}