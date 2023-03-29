package com.music.topalbums.clientapi.albums.model


class ArtistSongsCollection
{
    var resultCount = 0
    var results: ArrayList<ArtistSong>? = null
}

class ArtistSong
{
    var wrapperType: String? = null
    var artistType: String? = null
    var artistName: String? = null
    var artistLinkUrl: String? = null
    var artistId = 0
    var amgArtistId = 0
    var primaryGenreName: String? = null
    var primaryGenreId = 0
    var kind: String? = null
    var collectionId = 0
    var trackId = 0
    var collectionName: String? = null
    var trackName: String? = null
    var collectionCensoredName: String? = null
    var trackCensoredName: String? = null
    var artistViewUrl: String? = null
    var collectionViewUrl: String? = null
    var trackViewUrl: String? = null
    var previewUrl: String? = null
    var artworkUrl30: String? = null
    var artworkUrl60: String? = null
    var artworkUrl100: String? = null
    var collectionPrice = 0.0
    var trackPrice = 0.0
    var releaseDate: String? = null  //Date
    var collectionExplicitness: String? = null
    var trackExplicitness: String? = null
    var discCount = 0
    var discNumber = 0
    var trackCount = 0
    var trackNumber = 0
    var trackTimeMillis = 0
    var country: String? = null
    var currency: String? = null
    var isStreamable = false
}
