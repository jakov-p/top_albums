package com.music.topalbums.ui.songs

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.*
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.ui.songs.helpers.ParamsHandler
import com.music.topalbums.utilities.Utilities
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
class SongFragmentListOkTest
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
    fun test_list_loaded_correctly()
    {
        onView(allOf(withId(R.id.start_text_view), withText(containsString("1.")))).perform(waitUntilVisible(5000))

        onView(allOf(withId(R.id.mid_text_view), withText(containsString("Nothing Left To Lose"))))
        onView(allOf(withId(R.id.end_text_view), withText(containsString(Utilities.composeDurationText(224760)))))
    }


    @Test
    fun test_list_shown_correctly()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())))
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("album_songs.json")).getServiceApi()
    }

}