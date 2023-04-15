package com.music.topalbums.ui.topalbums

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.italic
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.data.albums.Album
import com.music.topalbums.databinding.ItemBinding
import com.music.topalbums.utilities.ClickListenerHandler
import com.music.topalbums.utilities.Utilities.loadImage

/**
 * Defines the look of the album recycle view item.
 *
 * The look changes as a new search text is entered by the user. Any item that contains the
 * search text will have its content highlighted.
 * Only two album fields are searched for the search text, they are 'artist name' and 'album name'.
 * Only the part of the text matching the search string will be highlighted, the rest stays in the
 * same font.
 *
 * @param onSelectedItem = called when the user clicks on an album item
 */
class AlbumsListAdapter(val onSelectedItem:(album: Album) -> Unit): PagingDataAdapter<Album, AlbumsListAdapter.AlbumListViewHolder>(DiffCallback)
{
    private var searchText:String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListViewHolder
    {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
     * The user has entered a new search text, adapt the look to this change
     * @param searchText
     */
    fun applySearch(searchText:String?)
    {
        this@AlbumsListAdapter.searchText = searchText
    }

    inner class AlbumListViewHolder(val binding: ItemBinding) : ViewHolder(binding.root)
    {
        fun bind(album: Album, position: Int)
        {
            val releaseDateShortened = album.releaseDate?.split("T")?.get(0)

            val restText = SpannableStringBuilder()
                .append("price =  ${album.collectionPrice} ${album.currency}")
                .append("\n")
                .append("date =  ${releaseDateShortened}")
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

                //make this item double clickable
                ClickListenerHandler(binding.root, onSelectedItem).setDoubleClickListener(album)
            }
        }

        /**
         * Looks for the search text in the given field. If found, that part of the
         * field text will be highlighted, and the rest will stay as it was.
         *
         * @param field text to be searched for a search text
         * @return  SpannableStringBuilder - text with parts in different style
         *          Boolean - true if the search text was found, false if not
         */
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

                    //the search text is found --> return the colored text
                    return Pair(textInColor, true)
                }
            }

            //the search text was not found --> return the text as is
            return Pair(SpannableStringBuilder(field), false)
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