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
 * If the active filter contains neither  genre nor release time criteria then a button will be shown.
 *
 * The event 'onClicked' is called on click on any of the controls mentioned above.
 */
class FilterDisplayer(val binding: ViewFilterDisplayerBinding, val onClicked: () -> Unit)
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
                val chip = view as Chip
                chip.visibility = if (chip.tagAsReleaseTimeCriteria() == albumFilter.releaseTimeCriteria) View.VISIBLE else View.GONE

                chip.setOnClickListener {
                    chip.isChecked = false //we do not want it to look checked
                    onClicked()
                }
            }

            //show one or zero release time criteria chips
            genreChipGroupInclude.genreChipGroup.children.forEach { view ->
                val chip = view as Chip
                chip.visibility = if (chip.tagAsGenre() == albumFilter.genre) View.VISIBLE else View.GONE

                chip.setOnClickListener {
                    chip.isChecked = false //we do not want it to look checked
                    onClicked()
                }
            }

            //shown the button only if an empty filter (so that the user is offered some clickable control )
            with(binding.setFilterButton)
            {
                visibility = if (albumFilter.isEmpty()) View.VISIBLE else View.GONE
                setOnClickListener{onClicked() }
            }
        }
    }
}