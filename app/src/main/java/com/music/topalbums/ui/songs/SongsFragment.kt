package com.music.topalbums.ui.songs

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.databinding.FragmentSongsBinding
import com.music.topalbums.logger.Logger.loggable
import com.music.topalbums.ui.common.ListLoadStateListener
import com.music.topalbums.ui.songs.helpers.FloatingButtonsHandler
import com.music.topalbums.ui.songs.helpers.ParamsHandler
import com.music.topalbums.ui.songs.player.PlayerBottomSheet
import com.music.topalbums.ui.songs.player.PlayerBottomSheet.IEventListener
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.utilities.Utilities.initToolbar
import com.music.topalbums.utilities.Utilities.loadImage
import com.music.topalbums.utilities.Utilities.showShortToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The classes with  @AndroidEntryPoints don’t show up in Jacoco’s code coverage reports as covered,
 * so this hack with inheritance is a way how to include them.
 * See https://medium.com/livefront/dagger-hilt-testing-injected-android-components-with-code-coverage-30089a1f6872
 */
@AndroidEntryPoint
class SongsFragment : SongsFragmentImpl()


/**
 * Shows the list of songs on an album. The album is passed as a parameter to the fragment.
 *
 * It offers playing a particular song, going to the  album's and going to the artist's web page.
 */
open class SongsFragmentImpl : Fragment()
{
    val TAG = SongsFragmentImpl::class.java.simpleName

    private lateinit var binding : FragmentSongsBinding

    //album for which the songs are to be shown
    private val album: Album by lazy { requireNotNull( ParamsHandler.getAlbum(arguments))}

    // have we come here from 'TopAlbumsFragment'
    private val isFromTopAlbums: Boolean by lazy { requireNotNull( ParamsHandler.isFromTopAlbums(arguments))}

    @Inject
    lateinit var songsViewModelFactory: SongsViewModel.ISongsViewModelFactory

    private val viewModel: SongsViewModel by lazy{
        val providerFactory = SongsViewModel.ProviderFactory(songsViewModelFactory, album)
        ViewModelProvider(this, providerFactory )[SongsViewModel::class.java]
    }

    //the GUI control containing the album's songs
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
        FloatingButtons()
    }

    fun init()
    {
        songListAdapter = SongListAdapter(requireContext()) {

            if (!PlayerBottomSheet.isShownOnScreen)
            {
                PlayerBottomSheet.isShownOnScreen = true
                if (it.previewUrl != null)
                {
                    //show the GUI for playing a song
                    val bottomSheetFragment = PlayerBottomSheet(it)
                    bottomSheetFragment.show(requireActivity().supportFragmentManager, "PlayerDialogFragment")
                    bottomSheetFragment.eventListener = IEventListener { //fire when the 'PlayerBottomSheet' closes
                        songListAdapter.notifyPlayFinished()
                    }
                } else
                {
                    showShortToastMessage(requireContext(), "No song URL for this song.")
                }
            }
            else
            {
                loggable.w(TAG, "'PlayerBottomSheet' is already on the screen --> the click will be ignored")
            }
        }

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
        viewModel.album.collectionImageUrl?.let { binding.albumCoverImageView.loadImage( it) }

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

    /**
     * Floating buttons
     * Just groups the code related to floating buttons handling
     */
    private inner class FloatingButtons
    {
       init
        {
            /*
            Show the floating button command for going to the artist albums fragment only if we have come here from the 'TopAlbumsFragment'.
            The passed null value tells 'FloatingButtonsHandler' that we are not interested in that floating button .
            */
            val goToArtistAlbumsFragmentFunct = if (isFromTopAlbums) ::goToArtistAlbumsFragment else null
            FloatingButtonsHandler(binding.floatingButtonsInclude, binding.parentLayout, ::goToAlbumWebPage, goToArtistAlbumsFragmentFunct)
        }

        /** open the album's web page*/
        private fun goToAlbumWebPage()
        {
            viewModel.album.collectionViewUrl?.let {
                Utilities.openWebPage(requireActivity(), it)
            } ?:
                showShortToastMessage(requireContext(), "No web page for this album")
        }

        /** open the artist's web page */
        private fun goToArtistAlbumsFragment()
        {
            if(viewModel.artistInfo?.artistId !=null) {
                val bundle = com.music.topalbums.ui.artistalbums.helpers.ParamsHandler.createBundle(requireNotNull( viewModel.artistInfo))
                findNavController().navigate(R.id.action_songsFragment_to_artistAlbumsFragment, bundle)
            }
            else {
                showShortToastMessage(requireContext(), "Not possible to fetch the artist's albums.")
            }
        }
    }
}