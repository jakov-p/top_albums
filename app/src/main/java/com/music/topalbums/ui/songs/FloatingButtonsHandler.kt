package com.music.topalbums.ui.songs

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.core.text.scale
import com.music.topalbums.databinding.FloatingButtonsSongsBinding

/**
 * Creates and handles floating buttons  with commands
 *
 * @param floatingButtonsInclude binding
 * @param backgroundArea view that will be made half transparent when the hidden action buttons are shown
 * @param onGoToAlbumWeb  action when the 'Go to album web page' is clicked
 * @param onGoToArtistAlbums action when the 'Go to artist's albums fragment' is clicked
 */
class FloatingButtonsHandler(floatingButtonsInclude: FloatingButtonsSongsBinding, val backgroundArea:View,  onGoToAlbumWeb: ()-> Unit,  onGoToArtistAlbums: ()-> Unit)
{
    init
    {
        with(floatingButtonsInclude)
        {

            hideChildren()

            mainFirstFab.setOnClickListener{
                showChildren()
            }

            mainSecondFab.setOnClickListener{
                hideChildren()
            }

            albumWebFab.setOnClickListener {
                onGoToAlbumWeb()
            }
            artistAlbumsFab.setOnClickListener {
                onGoToArtistAlbums()
            }
        }
    }

    private fun FloatingButtonsSongsBinding.showChildren()
    {
        artistAlbumsFab.show()
        artistAlbumsTextView.visibility = View.VISIBLE

        albumWebFab.show()
        albumWebTextView.visibility = View.VISIBLE

        mainSecondFab.show()
        mainFirstFab.hide()

        backgroundArea.alpha = 0.4f
    }

    private fun FloatingButtonsSongsBinding.hideChildren()
    {
        artistAlbumsFab.hide()
        artistAlbumsTextView.visibility = View.GONE

        albumWebFab.hide()
        albumWebTextView.visibility = View.GONE

        mainSecondFab.hide()
        mainFirstFab.show()

        backgroundArea.alpha = 1.0f
    }

}