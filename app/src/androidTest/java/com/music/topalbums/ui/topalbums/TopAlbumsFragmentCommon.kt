package com.music.topalbums.ui.topalbums

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
open class TopAlbumsFragmentCommon
{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var navController: TestNavHostController

    @Before
    fun setup()
    {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        TopAlbumsApp.appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        hiltRule.inject()
        launchFragmentInHiltContainer<TopAlbumsFragment>(themeResId = R.style.Theme_Top_albums) {
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(requireView(), navController)

            navController.setCurrentDestination(R.id.topAlbumsFragment)
        }
    }

    companion object
    {
        fun getMockedServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("top_albums.json")).getServiceApi()
    }
}