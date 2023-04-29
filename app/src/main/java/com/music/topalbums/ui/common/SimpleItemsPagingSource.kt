package com.music.topalbums.ui.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.math.min

/**
 * A naive approach where it is assumed that all the pages are of the same size (except
 * for the last one). I did not want to complicate to implement a more complex approach
 * to cover the case where the initial load  size is different from the load size.
 *
 * @param Item (Album, Song, whatever...)
 * @property getCount method that returns the total number of the items
 * @property getItems method that returns the items in a specified range
 */
open class SimpleItemsPagingSource<Item:Any> constructor(
            val getCount: suspend ()-> Int,
            val getItems: suspend (fromIndex:Int, toIndex:Int)->List<Item>)
    : PagingSource<Int, Item>()
{
    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item>
    {
        try
        {
            val maxIndex = getCount()
            val maxPageNumber = maxIndex / params.loadSize;

            val pageNumber = params.key ?: 0
            val fromIndex = pageNumber * params.loadSize
            val toIndex = min((pageNumber + 1) * params.loadSize, maxIndex)

            val songs = getItems(fromIndex, toIndex)

            println("nextKey = " + if (pageNumber < maxPageNumber) pageNumber + 1 else null)
            return LoadResult.Page(
                data = songs,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = if (pageNumber < maxPageNumber) pageNumber + 1 else null,
                itemsBefore = fromIndex,
                itemsAfter = maxIndex - toIndex,
            )
        }
        catch (e: Exception)
        {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int?
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


