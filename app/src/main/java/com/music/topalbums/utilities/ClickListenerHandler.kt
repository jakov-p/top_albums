package com.music.topalbums.utilities

import android.view.View
import androidx.core.view.allViews
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener

/**
 * Reacts to a click (single or/and double or/and long click) on the provided root view or on
 * any of its children. Then it fires an 'onSelectedItem' event informing that the item was clicked.
 */
class ClickListenerHandler<T>(val root: View, val onSelectedItem:(item: T, position: Int) -> Unit)
{
    fun setClickListener(item: T, position:Int )
    {
        setSingleClickListener(item, position)
    }

    /**
     * The event will be fired if single clicked on the provided item's GUI controls
     */
    private fun setSingleClickListener(item: T, position:Int )
    {
        with(root)
        {
            root.setOnClickListener{onSelectedItem(item, position)}
            allViews.forEach {
                it.setOnClickListener{onSelectedItem(item, position)}
            }
        }
    }

    /**
     * The event will be fired if double clicked on the provided item's GUI controls
     */
    private fun setDoubleClickListener(item: T, position:Int )
    {
        val doubleClick = DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                //ignored
            }

            override fun onDoubleClick(view: View) {
                onSelectedItem(item, position)
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
    private fun setLongClickListener(item: T, position:Int)
    {
        with(root)
        {
            setOnLongClickListener {
                onSelectedItem(item, position)
                true
            }
            allViews.forEach {
                it.setOnLongClickListener {
                    onSelectedItem(item, position)
                    true
                }
            }
        }
    }
}