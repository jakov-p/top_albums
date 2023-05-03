package com.music.topalbums.ui.topalbums

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.music.topalbums.*
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.ui.topalbums.filter.AlbumFilter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class)
class TopAlbumsFragmentFilterTest: TopAlbumsFragmentCommon()
{
    @Test
    fun test_all_chips_are_present_in_dialog()
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
    fun test_release_time_enabler_checks_and_unchecks_chips()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).check(matches(isChecked()))

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).check(matches(CoreMatchers.not(isChecked())))
    }


    @Test
    fun test_genre_enabler_enabler_checks_and_unchecks_chips()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.ALTERNATIVE.name))).check(matches(isChecked()))

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.ALTERNATIVE.name))).check(matches(CoreMatchers.not(isChecked())))
    }

    @Test
    fun test_genre_filters_no_album()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.CLASSICAL.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(withId(R.id.no_results_text_view)).perform(waitUntilVisible(5000L))
    }

    @Test
    fun test_genre_filters_album()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.genre_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.Genre.COUNTRY.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(allOf(withId(R.id.top_text_view), withText(containsString("Luke Combs")))).check(matches( isDisplayed()))
    }

    @Test
    fun test_release_time_filters_no_album()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.OLDER_OVER_FIVE_YEARS.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(withId(R.id.no_results_text_view)).perform(waitUntilVisible(5000L))
    }

    @Test
    fun test_release_time_filters_album()
    {
        onView(withId(R.id.set_filter_fab)).perform(click())

        onView(withId(R.id.release_time_enabler_check_box)).perform(click())
        onView(withTagValue(equalTo(AlbumFilter.ReleaseTimeCriteria.NEWER_THAN_ONE_YEAR.name))).perform(click())
        Util.closeDialogByClickingOutside()

        onView(allOf(withId(R.id.top_text_view), withText(containsString("Luke Combs")))).perform(waitUntilVisible(2000))
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }

}