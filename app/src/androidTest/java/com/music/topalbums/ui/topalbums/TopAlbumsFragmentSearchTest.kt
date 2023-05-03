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
class TopAlbumsFragmentSearchTest: TopAlbumsFragmentCommon()
{

    @Test
    fun test_search()
    {
        onView(withId(R.id.search_edit_text)).perform(typeText("AAAAAASeasons"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(doesNotExist())

        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("Seas"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))


        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("ONs"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))
    }

    @Test
    fun test_search_shorter_than_3_ignored()
    {
        onView(withId(R.id.search_edit_text)).perform(typeText("AA"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(matches(isDisplayed()))

        onView(withId(R.id.search_edit_text)).perform(clearText())
        onView(withId(R.id.search_edit_text)).perform(typeText("AAA"))
        onView(allOf(withId(R.id.top_text_view), withText(containsString("72 Seasons")))).check(doesNotExist())
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }

}