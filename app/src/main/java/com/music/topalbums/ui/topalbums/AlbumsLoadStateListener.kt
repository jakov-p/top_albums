package com.music.topalbums.ui.topalbums

import android.content.Context
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.music.topalbums.R
import com.music.topalbums.databinding.FragmentTopAlbumsBinding
import com.music.topalbums.utilities.InternetConnectionChecker
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.logger.Logger.loggable

/**
 * Shows or hides GUI control displaying 'loading in progress' and error.
 */
class AlbumsLoadStateListener(private val context:Context, private val binding : FragmentTopAlbumsBinding, private val albumsListAdapter: AlbumsListAdapter )
{
    val TAG = AlbumsLoadStateListener::class.java.simpleName

    fun process(loadStates:CombinedLoadStates)
    {
        with(binding)
        {
            loggable.i(TAG, "A new CombinedLoadStates , loadStates =  " + loadStates.toString())

            // show an empty list
            val isListEmpty = (loadStates.refresh is LoadState.NotLoading) && albumsListAdapter.isEmpty()
            noResultsTextView.isVisible = isListEmpty

            // Only show the albums list if refresh is successful.
            albumsList.isVisible = loadStates.source.refresh is LoadState.NotLoading

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
            loggable.w(TAG, "An error has occurred, error = $errorMessage")

            if(!InternetConnectionChecker(context).isConnected)
            {
                loggable.w(TAG, "Internet connection failure.")
                binding.errorMessageTextView.isVisible = true
                binding.errorMessageTextView.text= context.getString(R.string.check_internet_connection)
            }
            else
            {
                binding.errorMessageTextView.isVisible = false
                Utilities.showLongToastMessage(context, errorMessage)
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
}