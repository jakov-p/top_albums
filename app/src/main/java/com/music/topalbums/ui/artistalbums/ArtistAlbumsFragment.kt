package com.music.topalbums.ui.artistalbums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.music.topalbums.R
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.clientapi.collection.ArtistInfo
import com.music.topalbums.databinding.FragmentArtistAlbumsBinding
import com.music.topalbums.ui.ListLoadStateListener
import com.music.topalbums.ui.common.FloatingButtonsHandler
import com.music.topalbums.ui.songs.SongsFragment
import com.music.topalbums.utilities.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Shows the list of top albums of a country
 * It offers country selection, filtering and search functionality.
 */
@AndroidEntryPoint
class ArtistAlbumsFragment : Fragment()
{
    private val TAG = ArtistAlbumsFragment::class.java.simpleName

    //private val artistInfo: ArtistInfo by lazy { requireNotNull( ParamsHandler.getArtistInfo(arguments))}
    private val artistInfo = ArtistInfo( 164449, "Artist Name Artist Name Artist Name Artist Name", null)

    @Inject
    lateinit var artistAlbumsViewModelFactory: ArtistAlbumsViewModel.IArtistAlbumsViewModelFactory

    private lateinit var binding : FragmentArtistAlbumsBinding

    private val viewModel: ArtistAlbumsViewModel by lazy{
        val providerFactory = ArtistAlbumsViewModel.ProviderFactory(artistAlbumsViewModelFactory, requireNotNull( artistInfo.artistId))
        ViewModelProvider(this,  providerFactory )[ArtistAlbumsViewModel::class.java]
    }

    //shows an item in the album list recycle view
    private lateinit var artistAlbumsListAdapter: ArtistAlbumsListAdapter

    //shows or hides GUI control displaying 'loading in progress' and error
    private lateinit var listLoadStateListener: ListLoadStateListener

    private val albumsList:RecyclerView
        get(){ return binding.listInclude.list }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentArtistAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(requireContext().getString(R.string.top_albums_fragment_title))
        init()
        initFloatingButtonsHandler()
    }

    fun init()
    {
        artistAlbumsListAdapter = ArtistAlbumsListAdapter(requireContext(), onSelectedItem =::goToSongsFragment)
        listLoadStateListener = ListLoadStateListener(requireContext(), binding.listInclude, artistAlbumsListAdapter)

        initalizeView()
        bindEvents()
    }

    private fun initalizeView() {

        // initialize recyclerView
        albumsList.setHasFixedSize(true)
        albumsList.adapter = artistAlbumsListAdapter

        //start collecting album list data to fill the adapter with it
        lifecycleScope.launch {
            viewModel.albums.collectLatest {
                artistAlbumsListAdapter.submitData(it)
            }
        }
        artistAlbumsListAdapter.addLoadStateListener(listLoadStateListener::process)
    }

    private fun bindEvents() {
        with(binding) {

            //'retryButton' is shown in case when album list loading fails
            listInclude.retryButton.setOnClickListener {
                artistAlbumsListAdapter.retry()
            }
        }
    }

    private fun initFloatingButtonsHandler()
    {
        FloatingButtonsHandler(binding.floatingButtonsInclude, binding.listInclude.root, ::goToArtistWebPage )
    }

    /** open the artist's web page */
    private fun goToArtistWebPage()
    {
        artistInfo.artistViewUrl?.let {
            Utilities.openWebPage(requireActivity(), it)
        } ?:
            Utilities.showShortToastMessage(requireContext(), "No web page for this artist")
    }


    private fun goToSongsFragment(album: Album, position: Int)
    {
        val bundle = SongsFragment.ParamsHandler.createBundle(album)
        findNavController().navigate(R.id.action_artistAlbumsFragment_to_songsFragment, bundle)
    }

    private fun initToolbar(title: String)
    {
        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(title)
        setHasOptionsMenu(false)
    }

    /**
     * Puts artist data into and extracts from a bundle.
     * Helps with the transfer of the artist parameters from one fragment to another.
     */
    object ParamsHandler
    {
        const val PARAM_ARTIST_INFO = "artist_info"

        fun getArtistInfo(bundle:Bundle?) : ArtistInfo? = bundle?.getParcelable(PARAM_ARTIST_INFO ) as ArtistInfo?

        fun createBundle(artistInfo: ArtistInfo): Bundle = Bundle().apply {
            putParcelable(PARAM_ARTIST_INFO, artistInfo)
        }
    }

}