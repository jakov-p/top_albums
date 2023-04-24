package com.music.topalbums.ui.songs

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.music.topalbums.data.songs.ISongsDataManager
import com.music.topalbums.clientapi.collection.Song
import kotlin.math.min

/**
 * A naive approach where it is assumed that all the pages are of the same size (except
 * for the last one). I did not want to complicate to implement a more complex approach
 * to cover the case where the initial load  size is different from the load size.
 */
class SongsPagingSource(val songsDataManager: ISongsDataManager) : PagingSource<Int, Song>()
{
    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song>
    {
        try
        {
            val maxIndex = songsDataManager.getSongsCount()
            val maxPageNumber = maxIndex / params.loadSize;

            val pageNumber = params.key ?: 0
            val fromIndex = pageNumber * params.loadSize
            val toIndex = min((pageNumber + 1) * params.loadSize, maxIndex)

            val songs = songsDataManager.getSongs(fromIndex, toIndex)

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


    override fun getRefreshKey(state: PagingState<Int, Song>): Int?
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