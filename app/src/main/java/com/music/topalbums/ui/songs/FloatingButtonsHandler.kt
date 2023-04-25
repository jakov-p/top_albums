package com.music.topalbums.ui.songs

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.core.text.scale
import com.music.topalbums.databinding.FloatingButtonsSongsBinding

/**
 * Creates and handles floating buttons  with commands
 *
 * There is a main button shown, which on its click shows or hides two additional floating buttons. These two child buttons
 * are actual command buttons.
 *
 * @param floatingButtonsInclude binding
 * @param onGoToAlbumWeb  action when the 'Go to album web page' is clicked
 * @param onGoToArtistWeb action when the 'Go to artist's web page' is clicked
 */
class FloatingButtonsHandler(floatingButtonsInclude: FloatingButtonsSongsBinding,  onGoToAlbumWeb: ()-> Unit,  onGoToArtistWeb: ()-> Unit)
{
    var eventListener: IEventListener? = null

    init
    {
        with(floatingButtonsInclude)
        {

            albumWebTextView.text = composeText("Album")
            artistWebTextView.text = composeText("Artist")

            hideChildren()

            //'mainFirstFab' is visible when the children are hidden, and then on its click,
            //it becomes replaced by  'mainSecondFab'
            mainFirstFab.setOnClickListener{
                showChildren()
                eventListener?.onChildrenShown()
            }

            //'mainSecondFab' is visible when the children are shown, and then on its click,
            //it becomes replaced by 'mainFirstFab'
            mainSecondFab.setOnClickListener{
                hideChildren()
                eventListener?.onChildrenHidden()
            }

            albumWebFab.setOnClickListener {
                onGoToAlbumWeb()
            }
            artistWebFab.setOnClickListener {
                onGoToArtistWeb()
            }
        }
    }

    /** make the main word stand out from the rest of the text */
    private fun composeText( mainWord :String) = SpannableStringBuilder().
                                                append("Go to ").
                                                bold { append(mainWord)}.
                                                append(" web page")


    private fun FloatingButtonsSongsBinding.showChildren()
    {
        artistWebFab.show()
        artistWebTextView.visibility = View.VISIBLE

        albumWebFab.show()
        albumWebTextView.visibility = View.VISIBLE

        //switch the main buttons on the screen
        mainSecondFab.show()
        mainFirstFab.hide()
    }

    private fun FloatingButtonsSongsBinding.hideChildren()
    {
        artistWebFab.hide()
        artistWebTextView.visibility = View.GONE

        albumWebFab.hide()
        albumWebTextView.visibility = View.GONE

        //switch the main buttons on the screen
        mainSecondFab.hide()
        mainFirstFab.show()
    }

    //notifies about the internal work of this class
    interface IEventListener
    {
        fun onChildrenShown()
        fun onChildrenHidden()
    }
}