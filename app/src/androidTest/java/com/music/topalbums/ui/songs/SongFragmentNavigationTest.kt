package com.music.topalbums.ui.songs

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.music.topalbums.*
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
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
class SongFragmentNavigationTest: SongFragmentCommon()
{
    @Test
    fun test_when_artist_albums_fab_is_clicked_then_go_to_artist_albums_fragment()
    {
        onView(withId(R.id.main_first_fab)).perform(click())
        onView(withId(R.id.artist_albums_fab)).perform(click())

        assertEquals(R.id.artistAlbumsFragment, navController.currentDestination?.id)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }
}