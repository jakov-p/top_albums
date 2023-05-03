package com.music.topalbums.ui.artistalbums

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.music.topalbums.R
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.waitUntilVisible
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class)
class ArtistAlbumsFragmentNavigationTest: ArtistAlbumsFragmentCommon()
{
    @Test
    fun test_when_an_album_is_clicked_then_go_to_songs_fragment()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        val itemInList = allOf(withId(R.id.top_text_view), withText(containsString("In Between Dub")))
        onView(itemInList).perform(waitUntilVisible(5000))

        onView(itemInList).perform(longClick())
        assertEquals(R.id.songsFragment, navController.currentDestination?.id)
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }

}