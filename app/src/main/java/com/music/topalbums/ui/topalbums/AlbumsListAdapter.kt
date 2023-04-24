package com.music.topalbums.ui.topalbums

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.databinding.ItemBinding
import com.music.topalbums.utilities.ClickListenerHandler
import com.music.topalbums.utilities.Utilities.extractCleanAlbumName
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
class AlbumsListAdapter(val context: Context, val onSelectedItem:(album: Album, position: Int) -> Unit): PagingDataAdapter<Album, AlbumsListAdapter.AlbumListViewHolder>(DiffCallback)
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
            binding.albumCoverImageView.loadImage(album.collectionImageUrl!!)
            fillText(album)
            addClickListeners(album, position)
            alternateColor(position)
        }

        /**
         * Compose and show the text describing the given album
         * @param album
         */
        private fun fillText(album: Album)
        {
            with(binding)
            {
                val releaseDateShortened = album.releaseDate?.split("T")?.get(0)
                val (albumNameInColor, artistNameInColor) = highlightTextIfSearchFound(album)

                /* One under another:
                 *  Album Name
                 *  Artist Name
                 *  Genre
                 */
                topTextView.text = SpannableStringBuilder().
                                   append(albumNameInColor).append("\n").
                                   scale(0.8f) { append(artistNameInColor).append("\n") }.
                                   scale(0.6f) { append(album.primaryGenreName) }

                //TODO these two fields are not used, to remove later if no purpose for them is found
                middleTextView.visibility = View.GONE
                bottomTextView.visibility = View.GONE

                //position in the list
                lastTextView.text = "${album.originalPos?.plus( 1)}".padStart(2, ' ') //e.g. ' 5.', '15.'
            }
        }


        /***
         * Prepare the text for showing in GUI.
         * AlbumName and ArtistName are searched for existence of the search phrase and if found that
         * part of the text is highlighted in one of these two fields.
         * @return decorated AlbumName and ArtistName fields
         */
        private fun highlightTextIfSearchFound(album: Album): Pair<CharSequence, CharSequence?>
        {
            //the logic here is to prevent search in the artistName if it is already found in the album name
            val (albumNameInColor, isFoundInAlbumName) = composeFieldInColor(SpannableStringBuilder().append(extractCleanAlbumName(album)))
            val artistNameInColor = if (isFoundInAlbumName)
            {
                album.artistName //do not search here if already found
            } else
            {
                composeFieldInColor(SpannableStringBuilder().append("${album.artistName}")).first
            }
            return  Pair(albumNameInColor, artistNameInColor)
        }


        /**
         * Looks for the search text in the given field. If found, that part of the
         * field text will be highlighted, and the rest will stay as it was.
         *
         * @param field text to be searched for a search text
         * @return  SpannableStringBuilder - text with parts in different style
         *          Boolean - true if the search text was found, false if not
         */
        fun composeFieldInColor(field:SpannableStringBuilder?): Pair<SpannableStringBuilder, Boolean>
        {
            searchText?.lowercase()?.let {searchText->

                val startIndex: Int? = field?.toString()?.lowercase()?.indexOf(searchText)
                if(startIndex!= null && startIndex!=-1)
                {
                    val textInColor = SpannableStringBuilder()
                                       .append(field.subSequence(0, startIndex))
                                       .bold { scale(1.3f) {append(field.subSequence(startIndex, startIndex + searchText.length))} }
                                       .append(field.substring(startIndex + searchText.length))

                    //the search text is found --> return the colored text
                    return Pair(textInColor, true)
                }
            }

            //the search text was not found --> return the text as is
            return Pair(SpannableStringBuilder(field), false)
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