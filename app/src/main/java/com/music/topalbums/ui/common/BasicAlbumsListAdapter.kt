package com.music.topalbums.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.databinding.AlbumItemBinding
import com.music.topalbums.utilities.ClickListenerHandler
import com.music.topalbums.utilities.Utilities.loadImage

/**
 * Defines the look of the album recycle view item.
 *
 * @param onSelectedItem = called when the user clicks on an album item
 */
abstract class BasicAlbumsListAdapter(val context: Context, val onSelectedItem:(album: Album, position: Int) -> Unit):
    PagingDataAdapter<Album, BasicAlbumsListAdapter.AlbumListViewHolder>(DiffCallback)
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

    /**
     * Compose and show the text describing the given album
     * @param album
     */
    abstract fun fillText(album: Album, binding: AlbumItemBinding)

    inner class AlbumListViewHolder(val binding: AlbumItemBinding) : ViewHolder(binding.root)
    {
        fun bind(album: Album, position: Int)
        {
            album.collectionImageUrl?.let { binding.albumCoverImageView.loadImage(it) }
            fillText(album, binding)
            addClickListeners(album, position)
            alternateColor(position)
        }

        /** make this item double and long clickable */
        private fun addClickListeners(album: Album, position: Int)
        {
            ClickListenerHandler(binding.root, ::onSelectedAlbum).apply {
                setDoubleClickListener(album, position)
                setLongClickListener(album, position)
            }
        }

        private fun onSelectedAlbum(album: Album, position: Int)
        {
            //change color and send the event
            binding.root.setBackgroundColor(context.getColor(R.color.item_selection))
            onSelectedItem(album, position)
        }

        /**
         *  Update the background color according to the odd/even positions in the list.
         *  @param position position in the list
         */
        private fun alternateColor(position: Int)
        {
            val color = if (position % 2 == 0) context.getColor(R.color.item_normal)else context.getColor(R.color.item_normal_alternate)
            binding.root.setBackgroundColor(color)
        }


        fun bindPlaceHolder()
        {
            //nothing here to be done
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