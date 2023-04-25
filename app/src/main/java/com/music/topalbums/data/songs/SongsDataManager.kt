package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.collection.Album

class SongsDataManager(val album: Album): SongsRepository(requireNotNull( album.collectionId)), ISongsDataManager
{

}
