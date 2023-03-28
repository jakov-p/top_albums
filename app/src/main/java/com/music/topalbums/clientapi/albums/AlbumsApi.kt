package com.music.topalbums.clientapi.albums

import retrofit2.Response

object AlbumsApi
{
    val albumsRetrofitClient: AlbumsRetrofitClient = AlbumsRetrofitClient()

    suspend fun getList(country: String, limit: Int): Response<Feed>
    {
        return  albumsRetrofitClient.service.getList(country, limit)
    }
}