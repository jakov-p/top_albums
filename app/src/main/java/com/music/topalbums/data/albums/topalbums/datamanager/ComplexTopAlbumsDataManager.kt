package com.music.topalbums.data.albums.topalbums.datamanager

import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.albums.topalbums.repository.FilteredTopAlbumsRepository
import kotlinx.coroutines.*

/**
 * The algorithm here is complicated. It uses two lists. One ('small') list contains only a limited number
 * of albums fetched from the internet (up to 20), and the full list has all the albums.
 *
 * Why this approach ?
 *
 * Note that it is not possible to read only a part in the middle of the list from the internet
 * (e.g from 23th to 45th album in the list), but only a range starting at the beginning(e.g. from the 0th up to the 23th
 * or from the 0th up to the 45th album). This way it is not possible to have 'paging' directly at the data source - to
 * download from the internet the exact page that is about to be shown in GUI.
 *
 * So it is possible only to download the full  or a limited full list.
 *
 * Loading of the full list sometimes takes more than 5 seconds, which means that the user would wait until
 * the first item appears in GUI pretty long. Instead of waiting for the full list, this implementation first loads a smaller list
 * which takes less than 1 second (on average), and offers this in the GUI. Until the user scrolls down to the albums
 * outside of this small list, the full list should be already fetched (in the background) and it would be appended after
 * the small list's albums. This way the user will not notice that the full list is not loaded from the beginning,
 * and will have better experience.
 */
class ComplexTopAlbumsDataManager constructor(country: String): ITopAlbumsDataManager
{
    companion object
    {
        const val REDUCED_LIMIT = 8 //'small' list size (enough to fill 1-2 screens)
        const val FULL_LIMIT = 100 //full list limit (usually the Server returns much less)
    }

    //downloads the 'small' list
    val reducedTopAlbumsRepository = FilteredTopAlbumsRepository(country, REDUCED_LIMIT)

    //downloads the full list (it runs in the background, started as soon as possible)
    val fullTopAlbumsRepository = FilteredTopAlbumsRepository(country, FULL_LIMIT)
    lateinit var fullDeferredJob: Deferred<Unit>

    //true = the full list has been downloaded
    override var isFullyLoaded: Boolean = false

    init
    {
        startFullLoad()  //start as soon as possible

    }

    /** The total number of albums fetched from the internet
     *
     *  @param isForFullLoad false = read from the 'small' list, true = read from the 'full' list
     *
     *  When isForFullLoad = false:
     *  ----------------------------
     *  It will initiate the download of the 'small' list if it has not started yet.
     *  If download of the 'small' list is already started, it will suspend until finished.
     *  If 'small' list is already loaded, it will immediately  return.
     *
     *  When isForFullLoad = true:
     *  ----------------------------
     *  If the full list is not yet loaded, it will suspend until finished.
     *  If the full list is already loaded, it will immediately  return.
     */
    override suspend  fun getAlbumsCount(isForFullLoad:Boolean): Int
    {
        restartFullLoadIfNeeded()

        if(!isForFullLoad)
        {
            return reducedTopAlbumsRepository.getAlbumsCount()
        }
        else
        {
            fullDeferredJob.await() //wait until finished
            return fullTopAlbumsRepository.getAlbumsCount()
        }
    }

    /** Get a range of albums fetched from the internet
     *
     *  When isForFullLoad = false:
     *  ----------------------------
     *  It will initiate the download of the 'small' list if it has not started yet.
     *  If download of the 'small' list is already started, it will suspend until finished.
     *  If 'small' list is already loaded, it will immediately  return.
     *
     *  When isForFullLoad = true:
     *  ----------------------------
     *  If the full list is not yet loaded, it will suspend until finished.
     *  If the full list is already loaded, it will immediately  return.
     *
     * @param isForFullLoad false = read from the 'small' list, true = read from the 'full' list
     * @param fromIndex the starting index of the range
     * @param toIndex the ending index of the range (not included)
     * @return albums in the requested range
     */
    override suspend fun getAlbums(isForFullLoad:Boolean, fromIndex:Int, toIndex: Int): List<Album>
    {
        restartFullLoadIfNeeded()

        if(!isForFullLoad)
        {
            return reducedTopAlbumsRepository.getAlbums(fromIndex, toIndex)
        }
        else
        {
            fullDeferredJob.await() //wait until finished
            return fullTopAlbumsRepository.getAlbums(fromIndex, toIndex)
        }
    }

    private fun startFullLoad()
    {
        //started in the background
        //TODO fix scope
        fullDeferredJob = CoroutineScope(Dispatchers.IO).async {
            fullTopAlbumsRepository.getAlbumsCount()
            isFullyLoaded = true
        }

    }

    /**
     * Restarts the download of the full list.
     * This can happen if internet connection fails on the first attempt.
     **/
    private fun restartFullLoadIfNeeded()
    {
        if(!fullDeferredJob.isActive && isFullyLoaded == false)
        {
            println("Restarting the full load...")
            startFullLoad()
        }
    }

    //this method will be called on any user's change of the filter
    override var filter: ((album: Album)-> Boolean)? = null
        set(value) {
            //the filter must be applied on both lists
            field = value
            reducedTopAlbumsRepository.filter = field
            fullTopAlbumsRepository.filter = field
        }

}