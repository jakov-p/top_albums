package com.music.topalbums.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.music.topalbums.utilities.Utilities.loadImage
import com.music.topalbums.utilities.Utilities.openWebPage
import com.music.topalbums.data.albums.Album
import com.music.topalbums.databinding.FragmentSongsBinding
import com.music.topalbums.ui.songs.player.PlayerBottomSheet
import com.music.topalbums.utilities.Utilities.showLongToastMessage
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
    private val viewModel: SongsViewModel by lazy{
        val album = ParamsHandler.getAlbum(arguments)
        val factory = SongsViewModel.Factory(album!!)
        ViewModelProvider(this, factory )[SongsViewModel::class.java]
    }


    private val songListAdapter: SongListAdapter = SongListAdapter({
        val bottomSheetFragment = PlayerBottomSheet(it)
        bottomSheetFragment.show(requireActivity().supportFragmentManager, "PlayerDialogFragment")
    })

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

        binding.albumCoverImageView.loadImage( viewModel.album.collectionImageUrl!!)
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
                showLongToastMessage(it.error.message.toString())
            }
        }
    }


    private fun bindEvents() {
        with(binding) {

            songsList.setHasFixedSize(true)

            retryButton.setOnClickListener {
                songListAdapter.retry()
            }

            albumWebButton.setOnClickListener{
                openWebPage(requireActivity(), viewModel.album.collectionViewUrl!!)
            }

            artistWebButton.setOnClickListener{
                viewModel.album.artistViewUrl?.let {
                    openWebPage(requireActivity(), it)
                }
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


    /**
     * Puts album object into and extracts from a bundle.
     * Helps with the transfer of the album parameter from one fragment to another.
     */
    object ParamsHandler
    {
        const val PARAM_NAME = "album"

        fun getAlbum(bundle:Bundle?) : Album? = bundle?.getParcelable(PARAM_NAME) as Album?

        fun createBundle(album: Album): Bundle = Bundle().apply {
            putParcelable(PARAM_NAME, album)
        }
    }

}