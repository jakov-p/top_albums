package com.music.topalbums.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.music.topalbums.R
import com.music.topalbums.utilities.Utilities.loadImage
import com.music.topalbums.utilities.Utilities.openWebPage
import com.music.topalbums.data.albums.Album
import com.music.topalbums.databinding.FragmentSongsBinding
import com.music.topalbums.ui.songs.player.PlayerBottomSheet
import com.music.topalbums.ui.ListLoadStateListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SongsFragment : Fragment()
{
    private lateinit var binding : FragmentSongsBinding

    private val viewModel: SongsViewModel by lazy{
        val album = ParamsHandler.getAlbum(arguments)
        val factory = SongsViewModel.Factory(album!!)
        ViewModelProvider(this, factory )[SongsViewModel::class.java]
    }

    private val songsList: RecyclerView
        get(){ return binding.listInclude.list }

    private val songListAdapter: SongListAdapter = SongListAdapter({
        val bottomSheetFragment = PlayerBottomSheet(it)
        bottomSheetFragment.show(requireActivity().supportFragmentManager, "PlayerDialogFragment")
    })

    //shows or hides GUI control displaying 'loading in progress' and error
    private lateinit var listLoadStateListener: ListLoadStateListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(requireContext().getString(R.string.songs_fragment_title))
        init()
    }

    fun init()
    {
        listLoadStateListener = ListLoadStateListener(requireContext(), binding.listInclude, songListAdapter)

        initalizeView()
        initalizeAdapter()
        bindEvents()
    }

    private fun initalizeView() {



        // initialize recyclerView
        songsList.adapter = songListAdapter

        lifecycleScope.launch {
            viewModel.songs.collectLatest {
                songListAdapter.submitData(it)
            }
        }

        binding.albumCoverImageView.loadImage( viewModel.album.collectionImageUrl!!)
        initalizeAdapter()
    }

    private fun initalizeAdapter() {

        songListAdapter.addLoadStateListener(listLoadStateListener::process)
    }


    private fun bindEvents() {
        with(binding) {

            songsList.setHasFixedSize(true)

            listInclude.retryButton.setOnClickListener {
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

    private fun initToolbar(title: String)
    {
        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(title)
        setHasOptionsMenu(false)
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