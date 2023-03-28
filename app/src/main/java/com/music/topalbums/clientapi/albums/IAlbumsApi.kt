package com.music.topalbums.clientapi.albums

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface  IAlbumsApi
{
    @GET("{country}/rss/topalbums/limit={limit}/json")
    suspend fun getList(@Path("country") country: String, @Path("limit") limit: Int): Response<Feed>
}