package com.music.topalbums.ui.artistalbums

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.collection.ArtistInfo
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.launchFragmentInHiltContainer
import com.music.topalbums.ui.artistalbums.helpers.ParamsHandler
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
class ArtistAlbumsFragmentTest
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        hiltRule.inject()

        val fragmentArgs =  ParamsHandler.createBundle(artistInfo)
        launchFragmentInHiltContainer<ArtistAlbumsFragment>(themeResId = R.style.Theme_Top_albums, fragmentArgs = fragmentArgs ){
        }

    }


    @Test
    fun test_upper_part_of_fragment()
    {
        onView(withId(R.id.decoration_image_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_text_view)).check(matches(withText(containsString(artistInfo.artistName))))
    }

    @Test
    fun test_float_buttons_as_initially()
    {
        onView(withId(R.id.main_first_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.main_second_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artist_web_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artist_web_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_float_buttons_in_action()
    {
        onView(withId(R.id.main_first_fab)).perform(click())

        onView(withId(R.id.main_first_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.main_second_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.artist_web_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.artist_web_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.main_second_fab)).perform(click())

        onView(withId(R.id.main_first_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.main_second_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artist_web_fab)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artist_web_text_view)).check(matches(not(isDisplayed())))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("artist_albums.json")).getServiceApi()
    }

    val artistInfo =  ArtistInfo( 12345, "Jack Johnson" , "https://music.apple.com/us/artist/jack-johnson/909253?uo=4")
}