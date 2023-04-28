package com.music.topalbums.data.albums.artistalbums

fun interface IArtistAlbumsDataManagerFactory
{
    fun create(artistId : Int): IArtistAlbumsDataManager
}