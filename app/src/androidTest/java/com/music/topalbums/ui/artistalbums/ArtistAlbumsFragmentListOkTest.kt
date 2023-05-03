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
import com.music.topalbums.utilities.DateConverter
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.waitUntilVisible
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
import java.time.format.DateTimeFormatter

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BindServiceApiModule::class)
class ArtistAlbumsFragmentListOkTest: ArtistAlbumsFragmentCommon()
{

//{"wrapperType":"collection", "collectionType":"Album", "artistId":909253, "collectionId":1677034579, "amgArtistId":468749, "artistName":"Jack Johnson", "collectionName":"In Between Dub", "collectionCensoredName":"In Between Dub", "artistViewUrl":"https://music.apple.com/us/artist/jack-johnson/909253?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/in-between-dub/1677034579?uo=4", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music116/v4/8c/52/cb/8c52cb3d-a7cf-0248-5df4-d38939cbda8b/23UMGIM25138.rgb.jpg/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music116/v4/8c/52/cb/8c52cb3d-a7cf-0248-5df4-d38939cbda8b/23UMGIM25138.rgb.jpg/100x100bb.jpg", "collectionExplicitness":"notExplicit", "trackCount":11, "copyright":"℗ 2023 Jack Johnson", "country":"USA", "currency":"USD", "releaseDate":"2023-06-02T07:00:00Z", "primaryGenreName":"Reggae"},
    @Test
    fun test_list_loaded_correctly()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        val formattedReleaseDate = DateConverter.fromStringToDate("2023-06-02T07:00:00Z").format(DateTimeFormatter.ofPattern("yyyy, MMMM"))

        onView(allOf(withId(R.id.top_text_view),
            allOf(withText(containsString("In Between Dub")),withText(containsString("Reggae")), withText(containsString(formattedReleaseDate))
            ))).perform(waitUntilVisible(5000))
    }


    @Test
    fun test_list_shown_correctly()
    {
        onView(withId(R.id.list)).perform(waitUntilVisible(5000))

        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.no_results_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressbar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class ProvideModule
    {
        @Provides
        fun  getServiceApi(): IServiceApi = getMockedServiceApi()
    }

}