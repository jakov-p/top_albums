package com.music.topalbums.ui.songs

import android.view.View
import androidx.core.view.allViews
import com.music.topalbums.data.songs.Song
import com.music.topalbums.databinding.SongItemBinding
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener

class ClickListenerHandler(val binding: SongItemBinding, val onSelectedItem:(song: Song) -> Unit)
{
    fun setListeners(song: Song)
    {
        setLongClickListener(song)
        setDoubleClickListener(song)
    }

    private fun setDoubleClickListener(song: Song)
    {
        val doubleClick = DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                // DO STUFF SINGLE CLICK
            }

            override fun onDoubleClick(view: View) {
                onSelectedItem(song)
            }
        })

        with(binding.root)
        {
            setOnClickListener(doubleClick)
            allViews.forEach {
                it.setOnClickListener(doubleClick)
            }
        }
    }

    private fun setLongClickListener(song: Song)
    {
        with(binding.root)
        {
            setOnLongClickListener {
                onSelectedItem(song)
                true
            }
            allViews.forEach {
                it.setOnLongClickListener {
                    onSelectedItem(song)
                    true
                }
            }
        }
    }
}