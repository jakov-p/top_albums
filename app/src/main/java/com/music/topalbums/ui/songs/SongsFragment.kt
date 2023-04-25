package com.music.topalbums.ui.songs

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.databinding.FragmentSongsBinding
import com.music.topalbums.ui.ListLoadStateListener
import com.music.topalbums.ui.songs.player.PlayerBottomSheet
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.utilities.Utilities.loadImage
import com.music.topalbums.utilities.Utilities.openWebPage
import com.music.topalbums.utilities.Utilities.showShortToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Shows the list of songs on an album. The album is passed as a parameter to the fragment.
 *
 * It offers playing a particular song, going to the  album's and going to the artist's web page.
 */
@AndroidEntryPoint
class SongsFragment : Fragment()
{
    private lateinit var binding : FragmentSongsBinding

    private val album by lazy { ParamsHandler.getAlbum(arguments)!!}

    @Inject
    lateinit var songsViewModelFactory: SongsViewModel.ISongsViewModelFactory

    private val viewModel: SongsViewModel by lazy{
        val providerFactory = SongsViewModel.ProviderFactory(songsViewModelFactory, album)
        ViewModelProvider(this, providerFactory )[SongsViewModel::class.java]
    }

    private val songsList: RecyclerView
        get(){ return binding.listInclude.list }

    private lateinit var songListAdapter: SongListAdapter

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
        initFloatingButtonsHandler()
    }

    fun init()
    {
        songListAdapter = SongListAdapter(requireContext(), onSelectedItem = {
            val bottomSheetFragment = PlayerBottomSheet(it)
            bottomSheetFragment.show(requireActivity().supportFragmentManager, "PlayerDialogFragment")
        })

        listLoadStateListener = ListLoadStateListener(requireContext(), binding.listInclude, songListAdapter)

        initalizeView()
        bindEvents()
    }

    private fun initalizeView()
    {
        //start collecting song list data to fill the adapter with it
        lifecycleScope.launch {
            viewModel.songs.collectLatest {
                songListAdapter.submitData(it)
            }
        }

        //album's cover image loading
        binding.albumCoverImageView.loadImage( viewModel.album.collectionImageUrl!!)

        //compose a formatted text with a few album fields written one under another
        binding.allTextView.text = SpannableStringBuilder().
                                   bold {append(Utilities.extractCleanAlbumName(album)).append("\n")}.
                                   scale(0.80f) {append(album.artistName).append("\n")}

        // initialize recyclerView
        songsList.adapter = songListAdapter
        songListAdapter.addLoadStateListener(listLoadStateListener::process)
    }


    private fun bindEvents() {
        with(binding) {

            songsList.setHasFixedSize(true)

            //'retryButton' is shown in case when songs list loading fails
            listInclude.retryButton.setOnClickListener {
                songListAdapter.retry()
            }
        }
    }

    private fun initFloatingButtonsHandler()
    {
        FloatingButtonsHandler(binding.floatingButtonsInclude, ::goToAlbumWebPage, ::goToArtistWebPage).apply {
            eventListener = object : FloatingButtonsHandler.IEventListener
            {
                override fun onChildrenShown()
                {
                    binding.parentLayout.alpha = 0.4f
                }

                override fun onChildrenHidden()
                {
                    binding.parentLayout.alpha = 1.0f
                }
            }
        }
    }

    /** open the artist's web page */
    private fun goToArtistWebPage()
    {
        viewModel.album.artistViewUrl?.let {
            openWebPage(requireActivity(), it)
        } ?:
           showShortToastMessage(requireContext(), "No web page for this artist")
    }

    /** open the album's web page*/
    private fun goToAlbumWebPage()
    {
        viewModel.album.collectionViewUrl?.let {
            openWebPage(requireActivity(), viewModel.album.collectionViewUrl!!)
        } ?:
            showShortToastMessage(requireContext(), "No web page for this album")
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