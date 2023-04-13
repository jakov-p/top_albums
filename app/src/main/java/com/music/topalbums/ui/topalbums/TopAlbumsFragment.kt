package com.music.topalbums.ui.topalbums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.music.topalbums.R
import com.music.topalbums.databinding.FragmentTopAlbumsBinding
import com.music.topalbums.ui.topalbums.filter.FilterBottomSheet
import com.music.topalbums.ui.topalbums.filter.FilterDisplayer
import com.music.topalbums.ui.topalbums.search.SearchHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TopAlbumsFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private lateinit var binding : FragmentTopAlbumsBinding

    private val viewModel: TopAlbumsViewModel by lazy{
        ViewModelProvider(this )[TopAlbumsViewModel::class.java]
    }

    private lateinit var filterDisplayer : FilterDisplayer
    private lateinit var searchHandler: SearchHandler

    private val albumsListAdapter: AlbumsListAdapter = AlbumsListAdapter{
        val bundle = Bundle()
        bundle.putParcelable("album", it)
        findNavController().navigate(R.id.action_topAlbumsFragment_to_songsFragment, bundle)
    }
    private lateinit var albumsLoadStateListener: AlbumsLoadStateListener

    fun init()
    {
        albumsLoadStateListener = AlbumsLoadStateListener(requireContext(), binding, albumsListAdapter)

        initalizeView()
        bindEvents()
    }

    private fun initalizeView() {

        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        // initialize recyclerView
        binding.albumsList.adapter = albumsListAdapter

        lifecycleScope.launch {
            viewModel.topAlbums.collectLatest {
                albumsListAdapter.submitData(it)
            }
        }

        albumsListAdapter.addLoadStateListener(albumsLoadStateListener::process)
    }


    private fun bindEvents() {
        with(binding) {

            albumsList.setHasFixedSize(true)

            retryButton.setOnClickListener {
                albumsListAdapter.retry()
            }

            with(selectorInclude.countryCodePicker)
            {
                setOnCountryChangeListener {

                    //albumsListAdapter.submitData(lifecycle, PagingData.empty())
                    //binding.albumsList.swapAdapter(albumsListAdapter, true)
                    binding.albumsList.scrollToPosition(0)

                    handler.postDelayed({
                        //to clear the recycleView control of the old stuff

                        binding.albumsList.adapter = null
                        binding.albumsList.adapter = albumsListAdapter

                        val countryName = selectedCountryName
                        val countryCodeName = selectedCountryNameCode
                        viewModel.startNewLoad(countryCodeName)
                    }, 500)
                }
            }


            filterDisplayer = FilterDisplayer(filterInclude, ::showFilterBottomSheet)
            filterDisplayer.applyFilter(viewModel.albumFilter)


            searchHandler = SearchHandler(searchInclude, {searchText ->

                albumsListAdapter.applySearch(searchText)
                //to clear the recycleView control of the old stuff
                binding.albumsList.adapter = null
                binding.albumsList.adapter = albumsListAdapter

                viewModel.applySearch(searchText)
            })
        }
    }


    private fun showFilterBottomSheet()
    {
        if(!FilterBottomSheet.isShownOnScreen)
        {
            val filterBottomSheetFragment = FilterBottomSheet(viewModel.albumFilter) {
                viewModel.applyFilter(it)
                filterDisplayer.applyFilter(it)

            }
            filterBottomSheetFragment.show(requireActivity().supportFragmentManager, "FilterDialogFragment")
        }
        else
        {
            println("'FilterBottomSheet' is already on the screen --> the click will be ignored")
        }
    }




}