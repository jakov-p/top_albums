package com.music.topalbums.data.songs

import com.music.topalbums.data.albums.Album

class SongsDataManager(val album: Album): SongsRepository(album.collectionId!!)
{
}
