package com.music.topalbums.ui.topalbums

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.music.topalbums.data.albums.Album
import com.music.topalbums.data.albums.topalbums.ITopAlbumsDataManager
import kotlin.math.min

//TODO maybe to change this naive approach where initial load size == load size
class AlbumsPagingSource(val topAlbumsDataManager: ITopAlbumsDataManager) : PagingSource<Int, Album>()
{
    override val jumpingSupported: Boolean = true

    private var firstPageSize:Int? = null
    private var lastNotFullLoadPageSize:Int? = null

    private fun calculateIndexNotFull(pageNumber:Int, pageSize:Int):Int
    {
        return  firstPageSize!! + (pageNumber-1) *pageSize
    }

    private fun calculateIndexFull(pageNumber:Int, pageSize:Int):Int
    {
        if(lastNotFullLoadPageSize != null)
        {
            return firstPageSize!! + lastNotFullLoadPageSize!! + (pageNumber - 2) * pageSize
        }
        else
        {
            return firstPageSize!! + (pageNumber - 1) * pageSize
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album>
    {
        try
        {
            val pageNumber = params.key ?: 0
            val fromIndex:Int
            val toIndex:Int
            val nextKey:Int?

            val isFullyLoaded = topAlbumsDataManager.isFullyLoaded
            val albumsCount = topAlbumsDataManager.getAlbumsCount(isFullyLoaded)
            val albums: List<Album>

            if (firstPageSize == null )
            {
                firstPageSize = min(params.loadSize, albumsCount)
                if(pageNumber != 0)
                {
                    println("firstPageSize == null && pageNumber != 0, pageNumber = $pageNumber")
                }
            }


            if(!isFullyLoaded)
            {
                if (pageNumber == 0)
                {
                    fromIndex = 0
                    toIndex = firstPageSize!! -1
                    albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                    println("pageNumber == 0")
                }
                else
                {
                    fromIndex = min(calculateIndexNotFull(pageNumber, params.loadSize), albumsCount)
                    toIndex = min(calculateIndexNotFull(pageNumber + 1, params.loadSize), albumsCount)
                    if(toIndex < albumsCount)
                    {
                        albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                        println("normal")
                    }
                    else
                    {
                        if(fromIndex < albumsCount)
                        {
                            lastNotFullLoadPageSize = albumsCount - fromIndex
                            albums = topAlbumsDataManager.getAlbums(false, fromIndex, toIndex)
                            println("zadnji")
                        }
                        else
                        {
                            albums = topAlbumsDataManager.getAlbums(true, fromIndex, toIndex)
                            println("prelazak na full")
                        }
                    }
                }
                nextKey = pageNumber + 1
            }
            else
            {
                if (pageNumber == 0)
                {
                    fromIndex = 0
                    toIndex = firstPageSize!! -1
                }
                else
                {
                    fromIndex = min(calculateIndexFull(pageNumber, params.loadSize), albumsCount)
                    toIndex = min(calculateIndexFull(pageNumber + 1, params.loadSize), albumsCount)
                }
                nextKey = if (toIndex < albumsCount) pageNumber + 1 else null
                albums = topAlbumsDataManager.getAlbums(true, fromIndex, toIndex)
            }


            return LoadResult.Page(
                data = albums,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = nextKey,
                itemsBefore = fromIndex,
                itemsAfter = albumsCount - toIndex,
            ).apply {
                println("index = ($fromIndex, $toIndex)")
                println("data.size = " + data.size)
                println("prevKey = " + prevKey)
                println("nextKey = " + nextKey)
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