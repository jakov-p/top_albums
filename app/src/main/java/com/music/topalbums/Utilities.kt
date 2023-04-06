package com.music.topalbums

import android.widget.ImageView
import com.bumptech.glide.Glide

object Utilities
{
    fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }

    fun formatTimeMinSec(timeInSec:Int):String
    {
        return "${timeInSec/60}".padStart(2, '0') + ":"  + "${timeInSec%60}".padStart(2, '0')
    }
}