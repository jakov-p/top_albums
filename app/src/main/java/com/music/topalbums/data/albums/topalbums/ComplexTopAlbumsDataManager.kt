package com.music.topalbums.data.albums.topalbums

import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository
import kotlinx.coroutines.*

class ComplexTopAlbumsDataManager(country: String): ITopAlbumsDataManager
{
    companion object
    {
        const val REDUCED_LIMIT = 8
        const val FULL_LIMIT = 100
    }

    val reducedTopAlbumsRepository = FilteredTopAlbumsRepository(country, REDUCED_LIMIT)

    val fullTopAlbumsRepository = FilteredTopAlbumsRepository(country, FULL_LIMIT)
    lateinit var fullDeferredJob: Deferred<Unit>

    override var isFullyLoaded: Boolean = false

    init
    {
        startFullLoad()
    }

    override suspend  fun getAlbumsCount(isForFullLoad:Boolean): Int
    {
        if(!isForFullLoad)
        {
            return reducedTopAlbumsRepository.getAlbumsCount()
        }
        else
        {
            fullDeferredJob.await()
            return fullTopAlbumsRepository.getAlbumsCount()
        }
    }

    override suspend fun getAlbums(isForFullLoad:Boolean, fromIndex:Int, toIndex: Int): List<Album>
    {
        if(!isForFullLoad)
        {
            return reducedTopAlbumsRepository.getAlbums(fromIndex, toIndex)
        }
        else
        {
            fullDeferredJob.await()
            return fullTopAlbumsRepository.getAlbums(fromIndex, toIndex)
        }
    }

    private fun startFullLoad()
    {
        fullDeferredJob = CoroutineScope(Dispatchers.IO).async {
            fullTopAlbumsRepository.getAlbumsCount()
            isFullyLoaded = true
        }
    }

    override var filter: ((album: Album)-> Boolean)? = null
        set(value) {
            field = value
            reducedTopAlbumsRepository.filter = field
            fullTopAlbumsRepository.filter = field
        }

}