package com.music.topalbums.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
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


    fun openWebPage( context: Context, url: String)
    {
        try
        {
            val adjustedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")){
                "http://" + url;
            } else {
                url
            }

            //TODO checking for browser is missing
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(adjustedUrl))
            context.startActivity(browserIntent)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }


    fun showToastMessage(context:Context, message: String?)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun Fragment.showToastMessage( message: String?) = Utilities.showToastMessage(this.requireContext(), message)

    val Thread.isMain get() = Looper.getMainLooper().thread == Thread.currentThread()
}