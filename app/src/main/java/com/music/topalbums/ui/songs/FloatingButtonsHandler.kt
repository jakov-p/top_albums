package com.music.topalbums.ui.songs

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.core.text.scale
import com.music.topalbums.databinding.FloatingButtonsSongsBinding

/**
 * Creates and handles floating buttons  with commands
 * There are two main buttons that control hiding/showing of the initially hidden  command buttons
 * (children buttons).
 *
 * @param floatingButtonsInclude binding
 * @param backgroundArea view that will be made half transparent when the hidden action buttons are shown
 * @param onGoToAlbumWeb  action when the 'Go to album web page' is clicked
 * @param onGoToArtistAlbums action when the 'Go to artist's albums fragment' is clicked,
 *                      null value tells that 'artistAlbumsFab' is not to be shown at all
 */
class FloatingButtonsHandler(floatingButtonsInclude: FloatingButtonsSongsBinding, val backgroundArea:View,
                             onGoToAlbumWeb: ()-> Unit, val onGoToArtistAlbums: (()-> Unit)?)
{
    init
    {
        with(floatingButtonsInclude)
        {

            //initially hide the command buttons
            hideChildren()

            //just a hide/show button
            mainFirstFab.setOnClickListener{
                showChildren()
            }

            //just a hide/show button
            mainSecondFab.setOnClickListener{
                hideChildren()
            }

            //a real action button
            albumWebFab.setOnClickListener {
                onGoToAlbumWeb()
            }

            //a real action button
            artistAlbumsFab.setOnClickListener {
                onGoToArtistAlbums?.invoke()
            }
        }
    }

    private fun FloatingButtonsSongsBinding.showChildren()
    {
        if(onGoToArtistAlbums!=null) //do not show because we do not want this command
        {
            artistAlbumsFab.show()
            artistAlbumsTextView.visibility = View.VISIBLE
        }

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