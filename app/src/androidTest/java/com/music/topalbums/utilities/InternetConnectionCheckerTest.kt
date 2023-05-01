package com.music.topalbums.utilities


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternetConnectionCheckerTest{

    @Test
    fun testNetworkCapabilities()
    {
        val hasNetworkCapabilities = InternetConnectionChecker(InstrumentationRegistry.getInstrumentation().targetContext).isConnected
        Assert.assertEquals(true, hasNetworkCapabilities)
    }

}