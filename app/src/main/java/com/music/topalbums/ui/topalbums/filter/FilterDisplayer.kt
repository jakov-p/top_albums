package com.music.topalbums.ui.topalbums.filter

import android.view.View
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.music.topalbums.databinding.ViewFilterDisplayerBinding

/***
 * Displays the currently active filter.
 *
 * If the active filter contains the genre criteria then a corresponding genre chip will be shown.
 * If the active filter contains the release time criteria then a corresponding release time chip will be shown.
 * If the active filter contains neither genre nor release time criteria then a button  will be shown.
 *
 * The event 'onClicked' is called on click on any of the controls mentioned above.
 */
class FilterDisplayer(val binding: ViewFilterDisplayerBinding)
{
    init
    {
        applyFilter(AlbumFilter.EMPTY) //at the start of the application no filter is active
    }

    fun applyFilter(albumFilter: AlbumFilter)
    {
        with(binding)
        {
            //show one or zero genre chips
            releaseTimeChipGroupInclude.releaseTimeChipGroup.children.forEach { view ->
                (view as Chip).handleIt(view.tagAsReleaseTimeCriteria() == albumFilter.releaseTimeCriteria)
            }

            //show one or zero release time criteria chips
            genreChipGroupInclude.genreChipGroup.children.forEach { view ->
                (view as Chip).handleIt(view.tagAsGenre() == albumFilter.genre)
            }
        }
    }

    /**
     * Either hide it or show it as checked and make it clickable
     * @param isChosen
     */
    private fun Chip.handleIt(isChosen:Boolean)
    {
        if(isChosen)
        {
            visibility = View.VISIBLE
            isChecked = true
            setOnClickListener {
                isChecked = true //we do not want it to look unchecked (to have gray color)
            }
        }
        else
        {
            visibility = View.GONE
        }
    }
}