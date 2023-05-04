package com.music.topalbums.ui.songs

import android.view.View
import android.widget.EditText
import android.widget.TextView
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
class SongFragmentPlayingSongTest: SongFragmentCommon()
{

    @Test
    fun test_when_click_on_song_then_duration_view_disappears()
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
    fun test_when_player_dialog_opens_then_all_controls_present()
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
    fun test_when_song_starts_playing_then_all_controls_look_as_expected()
    {
        onView(allOf(withId(R.id.start_text_view), withText(containsString("1.")))).perform(longClick())
        waitUntilPlayerIsShown()

        onView(withId(R.id.play_button)).perform(waitUntilDisabled(2000)) //it means that the song has started playing
        onView(withId(R.id.pause_button)).check(matches(isEnabled()))
        onView(withId(R.id.stop_button)).check(matches(isEnabled()))

        val waitUntilTextAppears = WaitUntilConditionMet({ if(it is TextView) it.text.contains("Nothing Left To Lose") else false }, 2000)
        onView(withId(R.id.left_side_text_view)).perform(waitUntilTextAppears)
        onView(withId(R.id.right_side_text_view)).check(matches(withText(containsString("29")))) //each sample has 30 seconds (29+1)
    }

    @Test
    fun test_when_a_button_is_clicked_then_other_buttons_look_as_expected()
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

    private fun waitUntilPlayerIsShown()
    {
        onView(withId(R.id.play_button)).perform(waitUntilVisible(5000))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }
}