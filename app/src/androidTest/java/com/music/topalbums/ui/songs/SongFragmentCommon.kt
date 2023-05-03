package com.music.topalbums.ui.songs

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.Util
import com.music.topalbums.clientapi.MockedServiceApi
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.launchFragmentInHiltContainer
import com.music.topalbums.ui.songs.helpers.ParamsHandler
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
open class SongFragmentCommon
{

    val album: Album = Util.createAlbum()

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

    companion object
    {
        fun getMockedServiceApi(): IServiceApi = MockedServiceApi(MockedServiceApi.readFromAssets("album_songs.json")).getServiceApi()
    }
}