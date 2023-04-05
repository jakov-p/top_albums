package com.music.topalbums.clientapi.model


class AlbumSongsCollection
{
    var resultCount = 0
    var results: ArrayList<AlbumSong>? = null
}


class AlbumSong
{
    var wrapperType: String? = null
    var collectionType: String? = null
    var artistId: Int? = null
    var collectionId: Int? = null
    var amgArtistId: Int? = null
    var artistName: String? = null
    var collectionName: String? = null
    var collectionCensoredName: String? = null
    var artistViewUrl: String? = null
    var collectionViewUrl: String? = null
    var artworkUrl60: String? = null
    var artworkUrl100: String? = null
    var collectionPrice: Float? = null
    var collectionExplicitness: String? = null
    var trackCount: Int? = null
    var copyright: String? = null
    var country: String? = null
    var currency: String? = null
    var releaseDate: String? = null  //Date
    var primaryGenreName: String? = null
    var kind: String? = null
    var trackId: Int? = null
    var trackName: String? = null
    var trackCensoredName: String? = null
    var trackViewUrl: String? = null
    var previewUrl: String? = null
    var artworkUrl30: String? = null
    var trackPrice: Float? = null
    var trackExplicitness: String? = null
    var discCount: Int? = null
    var discNumber: Int? = null
    var trackNumber: Int? = null
    var trackTimeMillis: Int? = null
    var isStreamable = false
}