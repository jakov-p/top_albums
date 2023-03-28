package com.music.topalbums.clientapi.albums

import com.music.topalbums.clientapi.CallPerformer


object AlbumsRepository
{
    val TAG = AlbumsRepository::class.java.simpleName

    suspend fun getTopAlbums( country: String, limit: Int): Feed?
    {
        val callPerformer = CallPerformer("Get Top Albums Request"){ AlbumsApi.getList(country, limit) }
        return callPerformer.perform()
    }
}