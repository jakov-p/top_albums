package com.music.topalbums.ui.topalbums

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.TopAlbumsDataManager
import com.music.topalbums.data.albums.TopAlbumsDataManager_with_filter
import kotlin.io.path.fileVisitor
import kotlin.math.min

//TODO maybe to change this naive approach where initial load size == load size
class AlbumsPagingSource(val topAlbumsDataManager: TopAlbumsDataManager) : PagingSource<Int, Album>()
{
    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album>
    {
        try
        {
            val maxIndex = topAlbumsDataManager.getAlbumsCount()
            val maxPageNumber = maxIndex / params.loadSize;

            val pageNumber = params.key ?: 0
            val fromIndex = pageNumber * params.loadSize
            val toIndex = min((pageNumber + 1) * params.loadSize, maxIndex)

            val albums = topAlbumsDataManager.getAlbums(fromIndex, toIndex)

            println("nextKey = " + if (pageNumber < maxPageNumber) pageNumber + 1 else null)
            return LoadResult.Page(
                data = albums,
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