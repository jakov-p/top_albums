package com.music.topalbums.ui.songs

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
import com.music.topalbums.di.InternetConnectionModule
import com.music.topalbums.ui.songs.helpers.ParamsHandler
import com.music.topalbums.utilities.IInternetConnectionChecker
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
import org.mockito.Mockito


@HiltAndroidTest
@UninstallModules(BindServiceApiModule::class, InternetConnectionModule::class )
@RunWith(AndroidJUnit4::class)
class Test1: BasicSongFragmentListFailureTest()
{
    val jsonText:String = "aaaaaaa"
    val isInternetConnected = true

    @Test
    fun test_when_incorrect_json_then_show_retry_button()
    {
        onView(withId(R.id.retry_button)).perform(waitUntilVisible(5000L))

        onView(withId(R.id.list)).check(matches(not(isDisplayed())))

        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
        onView(withId(R.id.error_message_text_view)).check(matches(not(isDisplayed())))

        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))
    }


    @Module
    @InstallIn(SingletonComponent::class)
    inner class MockInternetConnectionModule
    {
        @Provides
        fun  getInternetConnectionChecker() = getMockedInternetChecker(isInternetConnected)
    }


    @Module
    @InstallIn(SingletonComponent::class)
    inner class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(jsonText).getServiceApi()
    }

}



@HiltAndroidTest
@UninstallModules(BindServiceApiModule::class, InternetConnectionModule::class )
@RunWith(AndroidJUnit4::class)
class Test2: BasicSongFragmentListFailureTest()
{
    val jsonText:String = """
                     {
                        "resultCount": 0,
                        "results": []
                     }
            """

    val isInternetConnected = true

    @Test
    fun test_when_empty_list_then_show_no_results_text()
    {
        onView(withId(R.id.no_results_text_view)).perform(waitUntilVisible(5000L))

        onView(withId(R.id.no_results_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_message_text_view)).check(matches(not(isDisplayed())))
    }


    @Module
    @InstallIn(SingletonComponent::class)
    inner class MockInternetConnectionModule
    {
        @Provides
        fun  getInternetConnectionChecker() = getMockedInternetChecker(isInternetConnected)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    inner class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(jsonText).getServiceApi()
    }


}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class, InternetConnectionModule::class )
class Test3: BasicSongFragmentListFailureTest()
{
    val jsonText:String = "aaaaaaa"
    val isInternetConnected = false

    @Test
    fun test_when_no_internet_then_show_no_internet()
    {
        onView(withId(R.id.retry_button)).perform(waitUntilVisible(5000L))

        onView(withId(R.id.list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
        onView(withId(R.id.error_message_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.error_message_text_view)).check(matches(withText( Util.getResourceString(R.string.check_internet_connection))))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    inner class MockInternetConnectionModule
    {
        @Provides
        fun  getInternetConnectionChecker() = getMockedInternetChecker(isInternetConnected)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    inner class SongsProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = MockedServiceApi(jsonText).getServiceApi()
    }
}




@HiltAndroidTest
open class BasicSongFragmentListFailureTest
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup()
    {
        hiltRule.inject()
        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        val fragmentArgs = ParamsHandler.createBundle(Util.createAlbum(), false)
        launchFragmentInHiltContainer<SongsFragment>(themeResId = R.style.Theme_Top_albums, fragmentArgs = fragmentArgs)
    }

    fun  getMockedInternetChecker(isConnected:Boolean): IInternetConnectionChecker = Mockito.mock(IInternetConnectionChecker::class.java).apply {
        Mockito.`when`(this.isConnected).thenReturn(isConnected)
    }
}



