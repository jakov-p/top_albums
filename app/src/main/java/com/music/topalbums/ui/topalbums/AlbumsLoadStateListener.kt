package com.music.topalbums.ui.topalbums

import android.content.Context
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.music.topalbums.databinding.FragmentTopAlbumsBinding
import com.music.topalbums.utilities.InternetConnectionChecker
import com.music.topalbums.utilities.Utilities

class AlbumsLoadStateListener(private val context:Context, private val binding : FragmentTopAlbumsBinding, private val albumsListAdapter: AlbumsListAdapter )
{
    fun process(loadStates:CombinedLoadStates)
    {
        with(binding)
        {
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
    {   // If any error, show a toast
        getIfAnyError(loadState)?.let {

            if(!InternetConnectionChecker(context).isConnected)
            {
                binding.errorMessageTextView.isVisible = true
                binding.errorMessageTextView.text= "Check internet connection" //TODO resources
            }
            else
            {
                binding.errorMessageTextView.isVisible = false
                Utilities.showToastMessage(context, it.error.message.toString())
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