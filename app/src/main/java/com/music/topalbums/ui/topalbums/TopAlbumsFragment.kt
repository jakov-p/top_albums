package com.music.topalbums.ui.topalbums

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.music.topalbums.R
import com.music.topalbums.data.albums.Album
import com.music.topalbums.databinding.FragmentTopAlbumsBinding
import com.music.topalbums.logger.Logger.loggable
import com.music.topalbums.ui.ListLoadStateListener
import com.music.topalbums.ui.songs.SongsFragment
import com.music.topalbums.ui.topalbums.filter.FilterBottomSheet
import com.music.topalbums.ui.topalbums.filter.FilterDisplayer
import com.music.topalbums.ui.topalbums.search.SearchHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Shows the list of top albums of a country
 * It offers country selection, filtering and search functionality.
 */
class TopAlbumsFragment : Fragment()
{
    private val TAG = TopAlbumsFragment::class.java.simpleName

    private lateinit var binding : FragmentTopAlbumsBinding

    private val viewModel: TopAlbumsViewModel by lazy{
        ViewModelProvider(this )[TopAlbumsViewModel::class.java]
    }

    //shows the current active filter
    private lateinit var filterDisplayer : FilterDisplayer

    //shows and offers editing of the current  search
    private lateinit var searchHandler: SearchHandler

    //shows an item in the album list recycle view
    private val albumsListAdapter: AlbumsListAdapter = AlbumsListAdapter(onSelectedItem =::goToSongsFragment)

    //shows or hides GUI control displaying 'loading in progress' and error
    private lateinit var listLoadStateListener: ListLoadStateListener

    private val albumsList:RecyclerView
        get(){ return binding.listInclude.list }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(requireContext().getString(R.string.top_albums_fragment_title))
        init()
    }

    fun init()
    {
        listLoadStateListener = ListLoadStateListener(requireContext(), binding.listInclude, albumsListAdapter)

        initalizeView()
        bindEvents()
    }

    private fun initalizeView() {

        // initialize recyclerView
        albumsList.setHasFixedSize(true)
        albumsList.adapter = albumsListAdapter

        //start collecting album list data to fill the adapter with it
        lifecycleScope.launch {
            viewModel.topAlbums.collectLatest {
                albumsListAdapter.submitData(it)
            }
        }
        albumsListAdapter.addLoadStateListener(listLoadStateListener::process)
    }

    private fun bindEvents() {
        with(binding) {

            //'retryButton' is shown in case when album list loading fails
            listInclude.retryButton.setOnClickListener {
                albumsListAdapter.retry()
            }

            //when the user selects a new country
            with(selectorInclude.countryCodePicker)
            {
                setOnCountryChangeListener {

                    loggable.i(TAG, "The user selected a new country, country  = $selectedCountryName ($selectedCountryNameCode)")
                    albumsList.scrollToPosition(0)
                    albumsList.visibility = View.INVISIBLE //so that this scroll is not visible to the user

                    handler.postDelayed({
                        albumsList.visibility = View.VISIBLE
                        clearAdapter()
                        viewModel.startNewLoad(selectedCountryNameCode)
                    }, 500) //long enough for scroll to happen

                    /*
                    This is delayed to give enough time for the recycle view control to scroll down to 0.
                    Without it, the scroll would not be performed at all, and the newly filled recycle view
                    (with the new country entries) would start at the position where the previous country was,
                    which creates small performance issues in work of 'AlbumsPagingSource'.
                    */
                }
            }

            //show the current active filter
            filterDisplayer = FilterDisplayer(filterInclude, ::showFilterBottomSheet)
            filterDisplayer.applyFilter(viewModel.albumFilter)

            //show and offer editing of the current  search
            searchHandler = SearchHandler(searchInclude, onSearchChanged = { searchText ->
                withBlinkPrevention {
                    loggable.i(TAG, "The user entered a new search text, search text = '$searchText'")
                    albumsListAdapter.applySearch(searchText)  //to highlight any found search text
                    //clearAdapter()
                    viewModel.applySearch(searchText) //to filter album list
                }
            })
        }
    }

    private fun withBlinkPrevention(block:()->Unit)
    {
        //it does not help consistently
        /*
        albumsListAdapter.removeLoadStateListener(listLoadStateListener::process)

        block.invoke()

        Handler(requireContext().mainLooper).postDelayed({
            albumsListAdapter.addLoadStateListener(listLoadStateListener::process)
        }, 300)
        */
    }

    //it seems it works better with this command
    private fun clearAdapter()
    {
        //to clear the recycleView control of the old stuff
        albumsList.adapter = null
        albumsList.adapter = albumsListAdapter
    }


    private fun showFilterBottomSheet()
    {
        if(!FilterBottomSheet.isShownOnScreen)
        {
            val filterBottomSheetFragment = FilterBottomSheet(viewModel.albumFilter, onClosed = {
                withBlinkPrevention {
                    filterDisplayer.applyFilter(it)
                    viewModel.applyFilter(it)
                }
            })
            filterBottomSheetFragment.show(requireActivity().supportFragmentManager, "FilterDialogFragment")
        }
        else
        {
            loggable.w(TAG, "'FilterBottomSheet' is already on the screen --> the click will be ignored")
        }
    }

    private fun goToSongsFragment(it: Album)
    {
        val bundle = SongsFragment.ParamsHandler.createBundle(it)
        findNavController().navigate(R.id.action_topAlbumsFragment_to_songsFragment, bundle)
    }

    private fun initToolbar(title: String)
    {
        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(title)
        setHasOptionsMenu(false)
    }
}