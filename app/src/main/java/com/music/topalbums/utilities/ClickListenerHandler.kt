package com.music.topalbums.utilities

import android.view.View
import androidx.core.view.allViews
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener

/**
 * Reacts to a double click (and/or a long click) on the provided root view or on
 * any of its children. Then it fires an 'onSelectedItem' event informing that the item was clicked.
 */
class ClickListenerHandler<T>(val root: View, val onSelectedItem:(item: T) -> Unit)
{
    /**
     * The event will be fired if double clicked on the provided item's GUI controls
     */
    fun setDoubleClickListener(item: T)
    {
        val doubleClick = DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                //ignored
            }

            override fun onDoubleClick(view: View) {
                onSelectedItem(item)
            }
        })

        with(root)
        {
            setOnClickListener(doubleClick)
            allViews.forEach {
                it.setOnClickListener(doubleClick)
            }
        }
    }

    /**
     * The event will be fired if long clicked on the provided item's GUI controls
     */
    fun setLongClickListener(item: T)
    {
        with(root)
        {
            setOnLongClickListener {
                onSelectedItem(item)
                true
            }
            allViews.forEach {
                it.setOnLongClickListener {
                    onSelectedItem(item)
                    true
                }
            }
        }
    }
}