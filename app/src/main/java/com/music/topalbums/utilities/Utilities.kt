package com.music.topalbums.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.music.topalbums.data.albums.Album


object Utilities
{
    /** load an image into an image view using Glide */
    fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }

    /**
     * Format time given in seconds into 'min:seconds' format
     * @return e.g. 309 --> "05:09"
     */
    fun formatTimeMinSec(timeInSec:Int):String
    {
        return "${timeInSec/60}".padStart(2, '0') + ":"  + "${timeInSec%60}".padStart(2, '0')
    }

    /**
     * Call a default browser to open a web page
     */
    fun openWebPage( context: Context, url: String)
    {
        try
        {
            val adjustedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")){
                "http://" + url;
            } else {
                url
            }

            //TODO checking for a default browser's presence is missing
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(adjustedUrl))
            context.startActivity(browserIntent)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

    //show a toast message of long duration
    fun showLongToastMessage(context:Context, message: String?)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun Fragment.showLongToastMessage(message: String?) = Utilities.showLongToastMessage(this.requireContext(), message)

    //show a toast message of short duration
    fun showShortToastMessage(context:Context, message: String?)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showShortToastMessage(message: String?) = Utilities.showShortToastMessage(this.requireContext(), message)


    //is the current thread the Main android thread?
    val Thread.isMain get() = Looper.getMainLooper().thread == Thread.currentThread()


    fun extractCleanAlbumName(album: Album): String?
    {
        //for some reason, the field 'collectionName' contains also the artist name at the end
        //it will be removed
        val extraStuff = " - ${album.artistName}"
        if(album.collectionName?.endsWith(extraStuff)?:false)
        {
            return album.collectionName?.removeSuffix(extraStuff)
        }
        else
        {
            return album.collectionName
        }
    }
}