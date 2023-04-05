package com.music.topalbums.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.music.topalbums.Utilities.loadImage
import com.music.topalbums.databinding.FragmentSongsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SongsFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private lateinit var binding : FragmentSongsBinding
    //val viewModel: SongsViewModel  by viewModel()
    private val viewModel: SongsViewModel by lazy{
        ViewModelProvider(this )[SongsViewModel::class.java]
    }


    private val songListAdapter: SongListAdapter = SongListAdapter()

    fun init()
    {
        initalizeView()
        initalizeAdapter()
        bindEvents()
    }

    private fun initalizeView() {

        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        // initialize recyclerView
        binding.songsList.adapter = songListAdapter

        lifecycleScope.launch {
            viewModel.songs.collectLatest {
                songListAdapter.submitData(it)
            }
        }

        binding.albumCoverImageView.loadImage( viewModel.album.artworkUrl!!)
        initalizeAdapter()
    }

    private fun initalizeAdapter() {

        songListAdapter.addLoadStateListener { loadState ->
            // show an empty list
            val isListEmpty = (loadState.refresh is LoadState.NotLoading) && songListAdapter.isEmpty()
            binding.noResultsTextView.isVisible = isListEmpty

            // Only show the albums list if refresh is successful.
            binding.songsList.isVisible = loadState.source.refresh is LoadState.NotLoading

            // Show the retry state in case of initial load or refresh failure
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Show the progress bar while loading
            binding.progressbar.isVisible = loadState.source.refresh is LoadState.Loading

            // If any error, show a toast
            getIfAnyError(loadState)?.let {
                showToastMessage(it.error.message.toString())
            }
        }
    }



    private fun bindEvents() {
        with(binding) {

            songsList.setHasFixedSize(true)

            retryButton.setOnClickListener {
                songListAdapter.retry()
            }
        }
    }

    protected open fun showToastMessage(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
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