package com.music.topalbums.data.albums.artistalbums

import com.music.topalbums.clientapi.collection.Album

/**
 * Fetches artist's albums from the internet.
 *
 * Provides partial fetching (paging) , although the list was downloaded from the internet as a whole
 *
 * @param artistId the ID of the artist for which the songs are fetched
 */
open class ArtistAlbumsDataManager(artistId : Int): ArtistAlbumsRepository(artistId), IArtistAlbumsDataManager
