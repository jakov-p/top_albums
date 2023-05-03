package com.music.topalbums.ui.artistalbums

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
import com.music.topalbums.clientapi.collection.ArtistInfo
import com.music.topalbums.databinding.FragmentArtistAlbumsBinding
import com.music.topalbums.ui.common.ListLoadStateListener
import com.music.topalbums.ui.artistalbums.helpers.FloatingButtonsHandler
import com.music.topalbums.ui.artistalbums.helpers.ParamsHandler
import com.music.topalbums.utilities.Utilities
import com.music.topalbums.utilities.Utilities.initToolbar
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
class ArtistAlbumsFragment : ArtistAlbumsFragmentImpl()

/**
 * Shows the list of all the albums of a particular artist.
 * The artist info is passed as a parameter to the fragment.
 */
open class ArtistAlbumsFragmentImpl : Fragment()
{
    private val TAG = ArtistAlbumsFragment::class.java.simpleName

    private val artistInfo: ArtistInfo by lazy { requireNotNull( ParamsHandler.getArtistInfo(arguments))}
    //private val artistInfo = ArtistInfo( 164449, "Artist Name Artist Name Artist Name Artist Name", null)

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

    //the GUI control containing the albums of the artist
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
        initToolbar(requireContext().getString(R.string.artist_albums_fragment_title))
        init()
        FloatingButtons()
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

        //compose a formatted text with the artist's name
        binding.allTextView.text = SpannableStringBuilder().
                                   scale(0.8f)  {append("Albums").append("\n")}.
                                   scale(0.6f)  {append("of").append("\n")}.
                                   bold { scale(1.5f)  {append(artistInfo.artistName)}}}


    private fun bindEvents() {
        with(binding) {
            //'retryButton' is shown in case when album list loading fails
            listInclude.retryButton.setOnClickListener {
                artistAlbumsListAdapter.retry()
            }
        }
    }



    private fun goToSongsFragment(album: Album, position: Int)
    {
        val bundle = com.music.topalbums.ui.songs.helpers.ParamsHandler.createBundle(album, false)
        findNavController().navigate(R.id.action_artistAlbumsFragment_to_songsFragment, bundle)
    }




    /**
     * Floating buttons
     * Just groups the code related to floating buttons handling
     */
    private inner class FloatingButtons
    {
        init
        {
            FloatingButtonsHandler(binding.floatingButtonsInclude, binding.listInclude.root, ::goToArtistWebPage)
        }

        /** open the artist's web page */
        private fun goToArtistWebPage()
        {
            artistInfo.artistViewUrl?.let {
                Utilities.openWebPage(requireActivity(), it)
            } ?: Utilities.showShortToastMessage(requireContext(), "No web page for this artist")
        }
    }
}