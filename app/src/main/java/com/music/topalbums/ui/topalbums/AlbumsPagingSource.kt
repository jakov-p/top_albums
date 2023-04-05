package com.music.topalbums.ui.topalbums

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.TopAlbumsDataManager
import kotlinx.coroutines.delay
import kotlin.math.min

class AlbumsPagingSource : PagingSource<Int, Album>()
{
    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album>
    {
        try
        {
            if(TopAlbumsDataManager.isLoaded)
            {
                val maxIndex = TopAlbumsDataManager.getAlbumsCount()
                val maxPageNumber = maxIndex / params.loadSize;

                val pageNumber = params.key ?: 0
                val fromIndex = pageNumber * params.loadSize
                val toIndex = min((pageNumber + 1) * params.loadSize, maxIndex)

                val albums = TopAlbumsDataManager.getAlbums(fromIndex, toIndex)

                println("nextKey = " + if (pageNumber < maxPageNumber) pageNumber + 1 else null)
                return LoadResult.Page(
                    data = albums,
                    prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                    nextKey = if (pageNumber < maxPageNumber) pageNumber + 1 else null,
                    itemsBefore = fromIndex,
                    itemsAfter = maxIndex - toIndex,
                )
            }
            else
            {
                println("TopAlbumsDataManager.isLoaded = " + TopAlbumsDataManager.isLoaded)
                delay(300)
                return LoadResult.Invalid()
                //return LoadResult.Error(Exception())
            }
        }
        catch (e: Exception)
        {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Album>): Int?
    {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        } ?: run {
            println("state.anchorPosition == null")
            0
        }
    }
}