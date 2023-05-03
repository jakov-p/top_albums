package com.music.topalbums.ui.songs

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
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
import org.hamcrest.Matcher
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


    lateinit var navController: TestNavHostController

    @Before
    fun setup() {

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        hiltRule.inject()

        val fragmentArgs =  ParamsHandler.createBundle(album , true)
        launchFragmentInHiltContainer<SongsFragment>(themeResId = R.style.Theme_Top_albums, fragmentArgs = fragmentArgs ){
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(requireView(), navController)

            navController.setCurrentDestination(R.id.songsFragment)
        }
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

        onView(withId(R.id.artist_albums_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.artist_albums_fab)).check(matches(isDisplayed()))




        onView(withId(R.id.main_second_fab)).perform(click())

        onView(withId(R.id.main_first_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.main_second_fab)).check(matches(not(isDisplayed())))

        onView(withId(R.id.album_web_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.album_web_text_view)).check(matches(not(isDisplayed())))

        onView(withId(R.id.artist_albums_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artist_albums_fab)).check(matches(not(isDisplayed())))
    }


    @Test
    fun play__1()
    {
        val durationView: Matcher<View> = allOf(withId(R.id.end_text_view), withText(containsString(Utilities.composeDurationText(224760))))

        onView(durationView).check(matches( isDisplayed()))
        onView(durationView).perform(longClick())

        waitUntilPlayerIsShown()
        waitForViewToDisappear(durationView, 2000)

        Util.closeDialogByClickingOutside()
        //onView(durationView).perform(waitUntilVisible(2000)) //?????
    }



    @Test
    fun play__2()
    {
        onView(allOf(withId(R.id.start_text_view), withText(containsString("1.")))).perform(longClick())
        waitUntilPlayerIsShown()

        onView(withId(R.id.play_button)).check(matches(isDisplayed()))
        onView(withId(R.id.pause_button)).check(matches(isDisplayed()))
        onView(withId(R.id.stop_button)).check(matches(isDisplayed()))

        onView(withId(R.id.left_side_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.right_side_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.seek_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun play__3()
    {
        onView(allOf(withId(R.id.start_text_view), withText(containsString("1.")))).perform(longClick())
        waitUntilPlayerIsShown()

        onView(withId(R.id.play_button)).perform(waitUntilDisabled(2000)) //it means that the song has started playing
        onView(withId(R.id.pause_button)).check(matches(isEnabled()))
        onView(withId(R.id.stop_button)).check(matches(isEnabled()))

        onView(withId(R.id.left_side_text_view)).check(matches(withText("Nothing Left To Lose")))
        onView(withId(R.id.right_side_text_view)).check(matches(withText(containsString("29")))) //each sample has 30 seconds (29+1)


    }

    @Test
    fun play__4()
    {
        onView(allOf(withId(R.id.start_text_view), withText(containsString("1.")))).perform(longClick())
        waitUntilPlayerIsShown()

        onView(withId(R.id.play_button)).perform(click())
        onView(withId(R.id.play_button)).check(matches(not(isEnabled())))
        onView(withId(R.id.pause_button)).check(matches(isEnabled()))
        onView(withId(R.id.stop_button)).check(matches(isEnabled()))

        onView(withId(R.id.pause_button)).perform(click())
        onView(withId(R.id.play_button)).check(matches(isEnabled()))
        onView(withId(R.id.pause_button)).check(matches(not(isEnabled())))
        onView(withId(R.id.stop_button)).check(matches(isEnabled()))
    }


    @Test
    fun navigation()
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
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("album_songs.json")).getServiceApi()
    }


    private fun waitUntilPlayerIsShown()
    {
        onView(withId(R.id.play_button)).perform(waitUntilVisible(5000))
    }

}