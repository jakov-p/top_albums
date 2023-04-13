package com.music.topalbums.ui.topalbums.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.music.topalbums.databinding.BottomSheetFilterSelectorBinding


class FilterBottomSheet(val initAlbumFilter: AlbumFilter, val onClosed: (AlbumFilter) -> Unit) : BottomSheetDialogFragment()
{
    private lateinit var binding: BottomSheetFilterSelectorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        isShownOnScreen = true
        binding = BottomSheetFilterSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init()
    {
        setFilter(initAlbumFilter)

        with(binding)
        {
            genreEnablerCheckBox.setOnClickListener {
                if ((it as CheckBox).isChecked)
                {
                    setGenreControls(AlbumFilter.Genre.ALTERNATIVE)
                } else
                {
                    setGenreControls(null)
                }
            }

            releaseTimeEnablerCheckBox.setOnClickListener {
                if ((it as CheckBox).isChecked)
                {
                    setReleaseTimeControls(AlbumFilter.ReleaseTime.NEWER_THAN_ONE_YEAR)
                } else
                {
                    setReleaseTimeControls(null)
                }
            }

        }
    }


    fun getFilter(): AlbumFilter
    {
        with(binding)
        {
            val releaseTimeSelectedChip = releaseTimeChipGroupInclude.releaseTimeChipGroup.children.firstOrNull() { view -> (view as Chip).isChecked } as Chip?
            val newerThan = releaseTimeSelectedChip?.tagAsReleaseTime()

            val genreSelectedChip = genreChipGroupInclude.genreChipGroup.children.firstOrNull() { view -> (view as Chip).isChecked } as Chip?
            val genre = genreSelectedChip?.tagAsGenre()

            return AlbumFilter(genre, newerThan)
        }
    }


    fun setFilter(albumFilter: AlbumFilter)
    {
        setGenreControls(albumFilter.genre)
        setReleaseTimeControls(albumFilter.releaseTime)
    }

    private fun setReleaseTimeControls(releaseTime: AlbumFilter.ReleaseTime?)
    {
        with(binding)
        {
            releaseTimeEnablerCheckBox.isChecked = releaseTime != null
            releaseTimeChipGroupInclude.releaseTimeChipGroup.enable( releaseTime != null)

            releaseTimeChipGroupInclude.releaseTimeChipGroup.children.forEach{ view ->
                (view as CheckBox).isChecked = (view.tagAsReleaseTime() == releaseTime)
            }

        }
    }

    private fun setGenreControls(genre: AlbumFilter.Genre?)
    {
        with(binding)
        {
            genreEnablerCheckBox.isChecked = genre != null
            genreChipGroupInclude.genreChipGroup.enable(genre != null)

            genreChipGroupInclude.genreChipGroup.children.forEach { view ->
                (view as CheckBox).isChecked = (view.tagAsGenre() == genre)
            }
        }
    }


    override fun onPause()
    {
        super.onPause()
        dismiss();
    }


    override fun onDismiss(dialog: DialogInterface)
    {
        onClosed(getFilter())
        super.onDismiss(dialog)
        isShownOnScreen = false
    }


    companion object
    {
        var isShownOnScreen :Boolean = false
    }
 }
