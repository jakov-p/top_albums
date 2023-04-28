package com.music.topalbums.ui.artistalbums

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.scale
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.databinding.AlbumItemBinding
import com.music.topalbums.ui.common.BasicAlbumsListAdapter
import com.music.topalbums.utilities.Utilities.extractCleanAlbumName

/**
 * Defines the look of the album recycle view item.
 *
 * @param onSelectedItem = called when the user clicks on an album item
 */
class ArtistAlbumsListAdapter(context: Context, onSelectedItem:(album: Album, position: Int) -> Unit):BasicAlbumsListAdapter(context,  onSelectedItem)
{
    /**
     * Compose and show the text describing the given album
     * @param album
     */
    override fun fillText(album: Album, binding: AlbumItemBinding)
    {
        with(binding)
        {
            val releaseDateShortened = album.releaseDate?.split("T")?.get(0)

            /* One under another:
             *  Album Name
             *  ???
             *  Genre
             */
            topTextView.text = SpannableStringBuilder().
                               append(album.collectionName).append("\n").
                               scale(0.8f) { append(releaseDateShortened).append("\n") }.
                               scale(0.6f) { append(album.primaryGenreName) }

            //TODO these two fields are not used, to remove later if no purpose for them is found
            middleTextView.visibility = View.GONE
            bottomTextView.visibility = View.GONE

            //position in the list
            lastTextView.text = "${album.originalPos?.plus( 1)}".padStart(2, ' ') //e.g. ' 5.', '15.'
        }
    }

}