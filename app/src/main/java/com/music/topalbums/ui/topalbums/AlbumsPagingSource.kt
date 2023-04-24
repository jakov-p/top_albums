package com.music.topalbums.ui.topalbums

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.music.topalbums.clientapi.collection.Album
import com.music.topalbums.data.albums.topalbums.datamanager.ITopAlbumsDataManager
import kotlin.math.min
import com.music.topalbums.logger.Logger.loggable

/**
 * The algorithm here is complicated. It uses two lists. One ('small') list contains only a limited number
 * of albums fetched from the internet (up to 20), and the full list has all the albums.
 *
 * The problem comes from the fact that it is not possible to read only a part in the middle of the list from the internet
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
 *
 * @param topAlbumsDataManager the provider of albums data
 */
class AlbumsPagingSource constructor(val topAlbumsDataManager: ITopAlbumsDataManager) : PagingSource<Int, Album>()
{
    val TAG = AlbumsPagingSource::class.java.simpleName

    override val jumpingSupported: Boolean = true

    //the size of the first page (it can be different from the size of other pages)
    private var firstPageSize:Int? = null

    /*The size of the last page when only small (not full) list was loaded.
    When the full list is finally fetched from internet then other pages will
    come after this page (and be bigger than this one)
    */
    private var lastNotFullLoadPageSize:Int? = null


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album>
    {
        try
        {
            loggable.d(TAG, "------------------------------------------------------------------------ START ---")
            loggable.d(TAG, params.toString())

            val pageNumber = params.key ?: 0

            var isFullyLoaded = topAlbumsDataManager.isFullyLoaded
            var albumsCount = topAlbumsDataManager.getAlbumsCount(isFullyLoaded)

            if (firstPageSize == null )
            {
                firstPageSize = min(params.loadSize, albumsCount)
                if(pageNumber != 0)
                {
                    //this says that the new album list is not shown starting with the page 0
                    loggable.w(TAG, "firstPageSize == null && pageNumber != 0, pageNumber = $pageNumber")
                }
            }

            if(!isFullyLoaded && isToSwitchToFullMode(albumsCount, pageNumber, params.loadSize))
            {
                loggable.d(TAG, "Switching to the 'full' mode.")

                //this method will suspend until the ful list is available
                albumsCount = topAlbumsDataManager.getAlbumsCount(true)
                isFullyLoaded = true
            }

            return doLoad(isFullyLoaded, albumsCount, pageNumber, params.loadSize)

        }
        catch (e: Exception)
        {
            return LoadResult.Error(e)
        }
        finally
        {
            loggable.d(TAG, "------------------------------------------------------------------------ END ---")
        }
    }

    private suspend fun doLoad(isFullyLoaded: Boolean, albumsCount: Int, pageNumber: Int, loadSize: Int):
            LoadResult.Page<Int, Album>
    {
        val fromIndex: Int
        val toIndex: Int
        val albums: List<Album>
        val nextKey: Int?

        if (!isFullyLoaded)
        {
            //NOT FULLY LOADED LIST FROM INTERNET (ONLY the 'SMALL' LIST IS AVAILABLE)
            //possible the most complex result =  firstPage + normalPage + ...+  normalPage + lastNotFullLoadPage
            val smallAlbumsCount = albumsCount

            if (pageNumber == 0)
            {
                fromIndex = 0
                toIndex = firstPageSize!!
                albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                loggable.d(TAG, "Not fully loaded. The first page, firstPageSize = $firstPageSize")
            }
            else
            {
                fromIndex = min(calculateIndexNotFull(pageNumber, loadSize), smallAlbumsCount)
                toIndex = min(calculateIndexNotFull(pageNumber + 1, loadSize), smallAlbumsCount)
                if (toIndex < smallAlbumsCount) //the page fits within  the 'small' list
                {
                    loggable.d(TAG, "Not fully loaded. A normal  page (not first, not last) in the 'small' list.")
                    albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                    //result so far =  firstPage + normalPage + ...+  normalPage
                }
                else
                {
                    if (fromIndex < smallAlbumsCount) //only a part of the page fits within  the 'small' list
                    {
                        loggable.d(TAG, "Not fully loaded. The last page in the 'small' list.")
                        lastNotFullLoadPageSize = smallAlbumsCount - fromIndex
                        albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                        //result so far  =  firstPage + normalPage + ...+  normalPage + lastNotFullLoadPage
                    }
                    else
                    {
                        //the page is fully beyond the 'small' list, we should not come here
                        loggable.e(TAG, "This indicates a bug. The page is fully beyond the 'small' list, we should not come here")
                        albums = listOf()
                    }
                }
            }
            //with the 'small' list there is always a page more to be loaded
            nextKey = pageNumber + 1
        }
        else
        {
            //FULLY LOADED LIST FROM INTERNET

            // possible the most complex result:
            // ------------------------------------
            // firstPage + normalPage + ...+  normalPage + lastNotFullLoadPage + normalPage + ...+  normalPage + lastFullLoadPage

            loggable.d(TAG, "Fully loaded. PageNumber = $pageNumber")
            if (pageNumber == 0)
            {
                fromIndex = 0
                toIndex = firstPageSize!!
            } else
            {
                fromIndex = min(calculateIndexFull(pageNumber, loadSize), albumsCount)
                toIndex = min(calculateIndexFull(pageNumber + 1, loadSize), albumsCount)
            }
            nextKey = if (toIndex < albumsCount) pageNumber + 1 else null
            albums = topAlbumsDataManager.getAlbums(true, fromIndex, toIndex)
        }


        return LoadResult.Page(
            data = albums,
            prevKey = if (pageNumber > 0) pageNumber - 1 else null,
            nextKey = nextKey,
            itemsBefore = fromIndex,
            itemsAfter = albumsCount - toIndex
        ).apply {
            loggable.i(TAG, "index = ($fromIndex, $toIndex)")
            loggable.i(TAG, "data.size = " + data.size)
            loggable.i(TAG, "prevKey = " + prevKey)
            loggable.i(TAG, "nextKey = " + nextKey)
        }
    }

    private fun isToSwitchToFullMode(smallAlbumsCount: Int, pageNumber: Int, loadSize: Int):  Boolean
    {
        if (smallAlbumsCount == 0)
        {
            /* No album in the 'small' list. The page is already beyond the list limits.
                   So we will wait here until the full list is fetched from the internet.
                */
            loggable.d(TAG, "Not fully loaded. The 'small' list is empty.")
            return true
        }
        else if (calculateIndexNotFull(pageNumber, loadSize) >= smallAlbumsCount)
        {
            /* This is the moment when we should take a page  beyond the last page of the small list.
                   It is impossible. So we will wait here until the full list is fetched from the internet.
                 */
            loggable.d(TAG, "Not fully loaded. This page goes beyond the limit of the 'small' list.")
            return true
        }

        return false
    }

    /**
     * Calculates the index in the 'small' list (the whole album list has not yet been fetched from the internet).
     *
     * @param pageNumber the number must be greater than 0
     * @param pageSize the size of the 'normal' pages (not firstPageSize, not lastNotFullLoadPageSize)
     * @return the index in the small (filtered) list
     */
    private fun calculateIndexNotFull(pageNumber:Int, pageSize:Int):Int
    {
        // firstPage + normalPage + ...+  normalPage
        return  firstPageSize!! + (pageNumber-1) * pageSize
    }

    /**
     * Calculates the index when the whole album list has been fetched from the internet.
     *
     * @param pageNumber the number must be greater than 0
     * @param pageSize the size of the 'normal' pages (not firstPageSize, not lastNotFullLoadPageSize)
     * @return the index in the full (filtered) list
     */
    private fun calculateIndexFull(pageNumber:Int, pageSize:Int):Int
    {
        if(lastNotFullLoadPageSize != null)
        {
            // firstPage + normalPage + ...+  normalPage + lastNotFullLoadPage + normalPage + ...+  normalPage
            return firstPageSize!! + lastNotFullLoadPageSize!! + (pageNumber - 2) * pageSize
        }
        else
        {
            // firstPage + normalPage + ...+  normalPage + normalPage + ...+  normalPage
            return firstPageSize!! + (pageNumber - 1) * pageSize
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Album>): Int?
    {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        } ?:
            run {
            println("state.anchorPosition == null")
            0
        }
    }
}