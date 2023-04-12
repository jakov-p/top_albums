package com.music.topalbums.ui.topalbums.filter

import android.view.View
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.music.topalbums.databinding.ViewFilterDisplayerBinding

class FilterDisplayer(val binding: ViewFilterDisplayerBinding, val onClicked: () -> Unit)
{
    init
    {
        applyFilter(AlbumFilter(null, null))
    }

    fun applyFilter(albumFilter: AlbumFilter)
    {
        with(binding)
        {
            releaseTimeChipGroupInclude.releaseTimeChipGroup.children.forEach { view ->
                val chip = view as Chip
                chip.visibility = if (chip.tagAsReleaseTime() == albumFilter.releaseTime) View.VISIBLE else View.GONE

                chip.setOnClickListener {
                    chip.isChecked = false
                    onClicked()
                }
            }

            genreChipGroupInclude.genreChipGroup.children.forEach { view ->
                val chip = view as Chip
                chip.visibility = if (chip.tagAsGenre() == albumFilter.genre) View.VISIBLE else View.GONE

                chip.setOnClickListener {
                    chip.isChecked = false
                    onClicked()
                }
            }

            with(binding.setFilterButton)
            {
                visibility = if (albumFilter.genre == null && albumFilter.releaseTime == null) View.VISIBLE else View.GONE
                setOnClickListener{onClicked() }
            }
        }
    }
}