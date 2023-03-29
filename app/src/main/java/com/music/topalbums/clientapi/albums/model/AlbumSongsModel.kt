package com.music.topalbums.clientapi.albums.model


class AlbumSongsCollection
{
    var resultCount = 0
    var results: ArrayList<AlbumSong>? = null
}


class AlbumSong
{
    var wrapperType: String? = null
    var collectionType: String? = null
    var artistId = 0
    var collectionId = 0
    var amgArtistId = 0
    var artistName: String? = null
    var collectionName: String? = null
    var collectionCensoredName: String? = null
    var artistViewUrl: String? = null
    var collectionViewUrl: String? = null
    var artworkUrl60: String? = null
    var artworkUrl100: String? = null
    var collectionPrice = 0.0
    var collectionExplicitness: String? = null
    var trackCount = 0
    var copyright: String? = null
    var country: String? = null
    var currency: String? = null
    var releaseDate: String? = null  //Date
    var primaryGenreName: String? = null
    var kind: String? = null
    var trackId = 0
    var trackName: String? = null
    var trackCensoredName: String? = null
    var trackViewUrl: String? = null
    var previewUrl: String? = null
    var artworkUrl30: String? = null
    var trackPrice = 0.0
    var trackExplicitness: String? = null
    var discCount = 0
    var discNumber = 0
    var trackNumber = 0
    var trackTimeMillis = 0
    var isStreamable = false
}