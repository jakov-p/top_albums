package com.music.topalbums.ui.common

import android.view.View
import com.music.topalbums.databinding.FloatingButtonsArtistAlbumsBinding
import com.music.topalbums.databinding.FloatingButtonsSongsBinding
import com.music.topalbums.ui.songs.FloatingButtonsHandler

/**
 * Creates and handles floating buttons  with commands
 *
 * There is a main button, which on its click shows or hides two additional floating buttons. These two child buttons
 * are actual command buttons.
 *
 * @param floatingButtonsInclude binding
 * @param onGoToArtistWeb action when the 'Go to artist's web page' is clicked
 */
class FloatingButtonsHandler(floatingButtonsInclude: FloatingButtonsArtistAlbumsBinding, val backgroundArea:View, onGoToArtistWeb: ()-> Unit)
{
    init
    {
        with(floatingButtonsInclude)
        {
            onHideChildren()

            mainFirstFab.setOnClickListener {
                onShowChildren()
            }

            mainSecondFab.setOnClickListener {
                onHideChildren()
            }

            artistWebFab.setOnClickListener {
                onGoToArtistWeb()
            }
        }
    }


    private fun FloatingButtonsArtistAlbumsBinding.onShowChildren()
    {
        artistWebFab.show()
        artistWebTextView.visibility = View.VISIBLE

        mainSecondFab.show()
        mainFirstFab.hide()

        backgroundArea.alpha = 0.4f
    }

    private fun FloatingButtonsArtistAlbumsBinding.onHideChildren()
    {
        artistWebFab.hide()
        artistWebTextView.visibility = View.GONE

        mainSecondFab.hide()
        mainFirstFab.show()

        backgroundArea.alpha = 1.0f
    }

}