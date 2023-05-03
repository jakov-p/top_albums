package com.music.topalbums.ui.artistalbums

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@HiltAndroidTest
open class ArtistAlbumsFragmentCommon
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val artistInfo =  ArtistInfo( 12345, "Jack Johnson" , "https://music.apple.com/us/artist/jack-johnson/909253?uo=4")

    lateinit var navController: TestNavHostController

    @Before
    fun setup() {

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        hiltRule.inject()

        val fragmentArgs =  ParamsHandler.createBundle(artistInfo)
        launchFragmentInHiltContainer<ArtistAlbumsFragment>(themeResId = R.style.Theme_Top_albums, fragmentArgs = fragmentArgs ){
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(requireView(), navController)

            navController.setCurrentDestination(R.id.artistAlbumsFragment)
        }

    }

    companion object
    {
        fun getMockedServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("artist_albums.json")).getServiceApi()
    }
}