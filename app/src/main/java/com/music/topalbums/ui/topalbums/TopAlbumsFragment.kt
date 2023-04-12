package com.music.topalbums.ui.topalbums

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
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
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

    fun init()
    {
        initalizeView()
        initalizeAdapter()
        bindEvents()
    }

    private fun initalizeView() {

        // initialize toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        initalizeAdapter()

        // initialize recyclerView
        binding.albumsList.adapter = albumsListAdapter

        lifecycleScope.launch {
            viewModel.topAlbums.collectLatest {
                albumsListAdapter.submitData(it)
            }
        }
    }

    private fun initalizeAdapter() {

        albumsListAdapter.addLoadStateListener { loadState ->
            // show an empty list
            val isListEmpty = (loadState.refresh is LoadState.NotLoading) && albumsListAdapter.isEmpty()
            binding.noResultsTextView.isVisible = isListEmpty

            // Only show the albums list if refresh is successful.
            binding.albumsList.isVisible = loadState.source.refresh is LoadState.NotLoading

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

            albumsList.setHasFixedSize(true)

            retryButton.setOnClickListener {
                albumsListAdapter.retry()
            }

            with(selectorInclude.countryCodePicker)
            {
                setOnCountryChangeListener {

                    //albumsListAdapter.submitData(lifecycle, PagingData.empty())
                    //binding.albumsList.swapAdapter(albumsListAdapter, true)

                    //to clear the recycleView control of the old stuff
                    binding.albumsList.adapter = null
                    binding.albumsList.adapter = albumsListAdapter

                    val countryName = selectedCountryName
                    val countryCodeName = selectedCountryNameCode
                    viewModel.startNewLoad(countryCodeName)
                }
            }

            filterDisplayer = FilterDisplayer(filterInclude) {
                val filterBottomSheetFragment = FilterBottomSheet(viewModel.albumFilter) {
                    viewModel.applyFilter(it)
                    filterDisplayer.applyFilter(it)
                }
                filterBottomSheetFragment.show(requireActivity().supportFragmentManager, "FilterDialogFragment")
            }
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