package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Album

class SongsDataManager(val album: Album): SongsRepository(album.collectionId!!), ISongsDataManager
{

}
