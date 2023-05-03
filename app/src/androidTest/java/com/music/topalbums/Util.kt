package com.music.topalbums

import android.support.test.uiautomator.UiDevice
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.clientapi.collection.Album


object Util
{

    fun closeDialogByClickingOutside()
    {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(100, 100)
    }


    fun getResourceString(id:Int) = InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(id)


    fun createAlbum() = Album(
        originalPos = 0,
        artistName =  "Jack Johnson",
        artistViewUrl = "https://music.apple.com/us/artist/jack-johnson/909253?uo=4",

        collectionImageUrl = "https://is3-ssl.mzstatic.com/image/thumb/Music118/v4/a6/b6/0a/a6b60ab2-37d9-53fe-7cee-3415eceaedb2/00602498430682.rgb.jpg/60x60bb.jpg",
        collectionViewUrl = "https://music.apple.com/us/album/wasting-time-single/1444409805?uo=4",

        primaryGenreName = "Pop",
        primaryGenreId = 5,

        collectionId = 1444409805,
        collectionName = "Wasting Time - Single",
        collectionPrice = 9.99f,
        currency = "USD",

        trackCount = 2,
        releaseDate = "2006-01-01T08:00:00Z"
    )
}