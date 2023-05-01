package com.music.topalbums.ui.songs

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.Util
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.launchFragmentInHiltContainer
import com.music.topalbums.ui.songs.helpers.ParamsHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class)
class SongFragmentTest
{

    val album:Album = Util.createAlbum()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        hiltRule.inject()

        val fragmentArgs =  ParamsHandler.createBundle(album , false)
        launchFragmentInHiltContainer<SongsFragment>(themeResId = R.style.Theme_Top_albums, fragmentArgs = fragmentArgs )
    }


    @Test
    fun test_list_loaded_successfully()
    {
        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_upper_part_of_fragment()
    {
        onView(withId(R.id.all_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_text_view)).check(matches(withText(containsString(album.artistName))));
        onView(withId(R.id.all_text_view)).check(matches(withText(containsString(album.collectionName))));
    }

    @Test
    fun test_float_buttons_as_initially()
    {
        onView(withId(R.id.main_first_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.main_second_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.album_web_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.album_web_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_float_buttons_in_action()
    {
        onView(withId(R.id.main_first_fab)).perform(click())

        onView(withId(R.id.main_first_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.main_second_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.album_web_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.album_web_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.main_second_fab)).perform(click())

        onView(withId(R.id.main_first_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.main_second_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.album_web_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.album_web_text_view)).check(matches(not(isDisplayed())))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("album_songs.json")).getServiceApi()
    }

}