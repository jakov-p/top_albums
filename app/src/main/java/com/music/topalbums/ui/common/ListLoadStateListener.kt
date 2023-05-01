package com.music.topalbums.ui.common

import android.content.Context
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.music.topalbums.R
import com.music.topalbums.TopAlbumsApp
import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.retrofit.ServerUrl
import com.music.topalbums.data.songs.SongsRepository
import com.music.topalbums.databinding.ViewAndProgressBinding
import com.music.topalbums.utilities.InternetConnectionChecker
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.logger.Logger.loggable
import com.music.topalbums.utilities.IInternetConnectionChecker
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 * Shows or hides GUI control displaying 'loading in progress' and error.
 */
class ListLoadStateListener(private val context:Context, private val binding: ViewAndProgressBinding, private val listAdapter: PagingDataAdapter<*,*>)
{
    val TAG = ListLoadStateListener::class.java.simpleName

    val internetConnectionChecker = EntryPoints.get(TopAlbumsApp.appContext, IListLoadStateListenerEntryPoint::class.java).getInternetConnectionChecker()

    fun process(loadStates:CombinedLoadStates)
    {
        with(binding)
        {
            loggable.i(TAG, "A new CombinedLoadStates , loadStates =  " + loadStates.toString())

            // show an empty list
            val isListEmpty = (loadStates.refresh is LoadState.NotLoading) && listAdapter.itemCount==0
            noResultsTextView.isVisible = isListEmpty

            // Only show the albums list if refresh is successful.
            list.isVisible = loadStates.source.refresh is LoadState.NotLoading

            // Show the retry state in case of initial load or refresh failure
            (loadStates.source.refresh is LoadState.Error).let{ isError: Boolean ->
                retryButton.isVisible = isError
                errorMessageTextView.isVisible = isError
            }

            // Show the progress bar while loading
            progressbar.isVisible = loadStates.source.refresh is LoadState.Loading

            handleIfError(loadStates)
        }
    }

    private fun handleIfError(loadState: CombinedLoadStates)
    {
        // If any general error show a toast or message in case of internet connection loss
        getIfAnyError(loadState)?.let {

            val errorMessage = it.error.message.toString()
            loggable.e(TAG, "An error has occurred, error = $errorMessage")

            if(!internetConnectionChecker.isConnected)
            {
                loggable.w(TAG, "Internet connection failure.")
                binding.errorMessageTextView.isVisible = true
                binding.errorMessageTextView.text= context.getString(R.string.check_internet_connection)
            }
            else
            {
                binding.errorMessageTextView.isVisible = false
                Utilities.showShortToastMessage(context, "An error has occurred when downloading data.")
                //Utilities.showShortToastMessage(context, "An error has occurred, " + errorMessage)
            }
        }
    }

    private fun getIfAnyError(loadState: CombinedLoadStates): LoadState.Error?
    {
        return when
        {
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            loadState.append  is LoadState.Error -> loadState.append  as LoadState.Error
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            else -> null
        }
    }


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface IListLoadStateListenerEntryPoint
    {
        fun getInternetConnectionChecker(): IInternetConnectionChecker
    }
}