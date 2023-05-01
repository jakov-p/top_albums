package com.music.topalbums.ui.topalbums

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.di.BindServiceApiModule
import com.music.topalbums.launchFragmentInHiltContainer
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
class TopAlbumsFragmentTest
{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        hiltRule.inject()
        launchFragmentInHiltContainer<TopAlbumsFragment>(themeResId = R.style.Theme_Top_albums )
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
    fun test_all_parts_are_present_initially()
    {
        onView(withId(R.id.decoration_image_view)).check(matches(isDisplayed()))
        onView(withId(R.id.filter_include)).check(matches(isDisplayed()))
        onView(withId(R.id.search_include)).check(matches(isDisplayed()))
        onView(withId(R.id.list_include)).check(matches(isDisplayed()))
        onView(withId(R.id.selector_include)).check(matches(isDisplayed()))
    }


    @Test
    fun test_float_buttons_as_initially()
    {
        onView(withId(R.id.set_filter_fab)).check(matches(isDisplayed()))
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("top_albums.json")).getServiceApi()
    }

}