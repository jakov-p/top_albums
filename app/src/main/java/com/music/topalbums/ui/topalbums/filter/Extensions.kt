package com.music.topalbums.ui.topalbums.filter

import android.view.View
import android.widget.CheckBox
import androidx.core.view.children
import com.google.android.material.chip.ChipGroup

//Tag (string) converted into a ReleaseTime filter criteria
fun View.tagAsReleaseTimeCriteria() = AlbumFilter.ReleaseTimeCriteria.valueOf(this.tag as String)

//Tag (string) converted into a Genre filter criteria
fun View.tagAsGenre() = AlbumFilter.Genre.valueOf(this.tag as String)

// Enable/disable all the children of the given chip group
fun ChipGroup.enable(isEnabled: Boolean) = this.children.forEach{ view ->
    (view as CheckBox).isEnabled = isEnabled
}






