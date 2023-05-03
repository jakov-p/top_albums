package com.music.topalbums.ui.topalbums

import android.R.attr.x
import android.R.attr.y
import android.support.test.uiautomator.UiDevice
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
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class)
class TopAlbumsFragmentTest
{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var navController: TestNavHostController

    @Before
    fun setup()
    {

        // Create a TestNavHostController
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        hiltRule.inject()
        launchFragmentInHiltContainer<TopAlbumsFragment>(themeResId = R.style.Theme_Top_albums) {
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun test_list_loaded_successfully()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())))
    }


    @Test
    fun test_all_parts_are_present_initially()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        onView(withId(R.id.decoration_image_view)).check(matches(isDisplayed()))
        onView(withId(R.id.filter_include)).check(matches(not(isDisplayed()))) //no filter selected yet, so now it is empty
        onView(withId(R.id.search_include)).check(matches(isDisplayed()))
        onView(withId(R.id.list_include)).check(matches(isDisplayed()))
        onView(withId(R.id.selector_include)).check(matches(isDisplayed()))
    }


    @Test
    fun test_float_buttons_as_initially()
    {
        onView(withId(R.id.set_filter_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun test_float_buttons_as_initially__()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        AlbumFilter.Genre.values().forEach {
            onView(withTagValue(equalTo(it.name))).check(matches(isDisplayed()))
        }

        AlbumFilter.ReleaseTimeCriteria.values().forEach {
            onView(withTagValue(equalTo(it.name))).check(matches(isDisplayed()))
        }
    }


    @Test
    fun test_float_buttons_as_initially___3()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).check(matches(isChecked()))

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).check(matches(not(isChecked())))
    }


    @Test
    fun test_float_buttons_as_initially___4()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.ALTERNATIVE.name))).check(matches(isChecked()))

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.ALTERNATIVE.name))).check(matches(not(isChecked())))
    }

    @Test
    fun test_float_buttons_as_initially___5()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.CLASSICAL.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(withId(R.id.no_results_text_view)).perform(waitUntilVisible(5000L))
    }

    @Test
    fun test_float_buttons_as_initially___6()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.COUNTRY.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(allOf(withId(R.id.top_text_view), withText(containsString("Luke Combs")))).check(matches( isDisplayed()))
    }

    @Test
    fun test_float_buttons_as_initially___51()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_FIVE_YEARS.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(withId(R.id.no_results_text_view)).perform(waitUntilVisible(5000L))
    }

    @Test
    fun test_float_buttons_as_initially___61()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(allOf(withId(R.id.top_text_view), withText(containsString("Luke Combs")))).perform(waitUntilVisible(2000))
    }

    @Test
    fun test_float_buttons_as_initially___search()
    {
        onView(withId(R.id.search_edit_text)).perform(typeText("AAAAAASeasons"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(doesNotExist())

        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("Seas"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))


        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("ONs"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))

        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("AA"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))

        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("AAA"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(doesNotExist())
    }

    @Test
    fun navigation()
    {
        val itemInList = allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))
        onView(itemInList).perform(waitUntilVisible(5000))

        onView(itemInList).perform(longClick())
        assertEquals(R.id.songsFragment, navController.currentDestination?.id)
    }



    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("top_albums.json")).getServiceApi()
    }

}