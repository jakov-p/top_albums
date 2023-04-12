package com.music.topalbums.ui.topalbums

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.italic
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
    private var searchText:String? = null

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

    fun applySearch(searchText:String?)
    {
        this@AlbumsListAdapter.searchText = searchText
    }

    inner class AlbumListViewHolder(val binding: AlbumItemBinding) : ViewHolder(binding.root)
    {
        fun bind(album: Album, position: Int)
        {
            val restText = SpannableStringBuilder()
                .append("price =  ${album.collectionPrice} ${album.currency}")
                .append("\n")
                .append("date =  ${album.releaseDate}")
                .append("\n")
                .append("genre =  ${album.primaryGenreName}")
                .append("\n")
                .italic { append("pos = $position") }

            //val restText = "price =  ${album.collectionPrice} ${album.currency} \n pos = $position "
            with(binding)
            {
                restTextView.text = restText
                nameTextView.text = composeFieldInColor("${album.collectionName}").first
                albumCoverImageView.loadImage(album.collectionImageUrl!!)

                setDoubleClickListener(album)
            }
        }

        fun composeFieldInColor(field:String? ): Pair<SpannableStringBuilder, Boolean>
        {
            searchText?.lowercase()?.let {searchText->

                val startIndex: Int? = field?.lowercase()?.indexOf(searchText)
                if(startIndex!= null && startIndex!=-1)
                {
                    val textInColor: SpannableStringBuilder = SpannableStringBuilder()
                        .append(field.subSequence(0, startIndex))
                        .bold { append(field.subSequence(startIndex, startIndex + searchText.length)) }
                        .append(field.substring(startIndex + searchText.length))
                    return Pair(textInColor, true)
                }
            }

            return Pair(SpannableStringBuilder(field), false)
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