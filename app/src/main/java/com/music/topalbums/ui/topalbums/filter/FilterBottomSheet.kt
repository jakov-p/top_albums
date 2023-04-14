package com.music.topalbums.ui.topalbums.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.music.topalbums.databinding.BottomSheetFilterSelectorBinding

/**
 * In this dialog the user can create a new album filter, which is then returned in the  'onClosed' event.
 *
 * The tag property on genre-related chip GUI controls is used for mapping the chip to a particular genre enumeration.
 * The tag property on release time criteria related chip GUI controls is used for mapping the chip
 * to a release time criteria enumeration.
 *
 * @param initAlbumFilter the current filter (to be shown and edited in the dialog)
 * @param onClosed called when the dialog finishes (it finishes by clicking outside the dialog)
 */
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
                    setGenreControls(AlbumFilter.Genre.ALTERNATIVE) //the default one is selected
                }
                else
                {
                    setGenreControls(null) //none is selected
                }
            }

            releaseTimeEnablerCheckBox.setOnClickListener {
                if ((it as CheckBox).isChecked)
                {
                    setReleaseTimeControls(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR) //the default one is selected
                }
                else
                {
                    setReleaseTimeControls(null) //none is selected
                }
            }
        }
    }


    /**
     * Go through the GUI control and conclude the filter out of it
     * @return
     */
    fun getFilter(): AlbumFilter
    {
        with(binding)
        {
            val releaseTimeSelectedChip = releaseTimeChipGroupInclude.releaseTimeChipGroup.children.firstOrNull{ view ->
                (view as Chip).isChecked } as Chip?
            val releaseTimeCriteria = releaseTimeSelectedChip?.tagAsReleaseTimeCriteria()

            val genreSelectedChip = genreChipGroupInclude.genreChipGroup.children.firstOrNull{ view ->
                (view as Chip).isChecked } as Chip?
            val genre = genreSelectedChip?.tagAsGenre()

            return AlbumFilter(genre, releaseTimeCriteria)
        }
    }

    //apply the filter onto GUI controls
    fun setFilter(albumFilter: AlbumFilter)
    {
        setGenreControls(albumFilter.genre)
        setReleaseTimeControls(albumFilter.releaseTimeCriteria)
    }

    /**
     * Set all release time-related GUI controls to reflect the current filter's release time criteria
     * @param releaseTimeCriteria e.g. within the last year, within the last month,...
     */
    private fun setReleaseTimeControls(releaseTimeCriteria: AlbumFilter.ReleaseTimeCriteria?)
    {
        //enable/disable the whole group and select the chip whose
        //tag is equal to the filter release time criteria
        with(binding)
        {
            releaseTimeEnablerCheckBox.isChecked = releaseTimeCriteria != null
            releaseTimeChipGroupInclude.releaseTimeChipGroup.enable( releaseTimeCriteria != null)

            releaseTimeChipGroupInclude.releaseTimeChipGroup.children.forEach{ view ->
                (view as CheckBox).isChecked = (view.tagAsReleaseTimeCriteria() == releaseTimeCriteria)
            }

        }
    }

    /**
     * Set all genre-related GUI controls to reflect the current filter's genre
     * @param genre e.g. pop, rock, country,..
     */
    private fun setGenreControls(genre: AlbumFilter.Genre?)
    {
        //enable/disable the whole group and select the chip whose
        //tag is equal to the filter genre
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
        dismiss()
    }


    override fun onDismiss(dialog: DialogInterface)
    {
        onClosed(getFilter())
        super.onDismiss(dialog)
        isShownOnScreen = false
    }


    companion object
    {
        //Keeps track if an instance of this dialog is currently shown on the screen.
        //The purpose of this is to prevent opening two dialogs at the same time
        var isShownOnScreen :Boolean = false
    }
 }
