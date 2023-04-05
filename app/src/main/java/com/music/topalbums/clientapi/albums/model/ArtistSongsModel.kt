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
    var artistId:Int? = null
    var amgArtistId:Int? = null
    var primaryGenreName: String? = null
    var primaryGenreId:Int? = null
    var kind: String? = null
    var collectionId:Int? = null
    var trackId:Int? = null
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
    var collectionPrice:Float? = null
    var trackPrice:Float? = null
    var releaseDate: String? = null  //Date
    var collectionExplicitness: String? = null
    var trackExplicitness: String? = null
    var discCount:Int? = null
    var discNumber:Int? = null
    var trackCount:Int? = null
    var trackNumber:Int? = null
    var trackTimeMillis:Int? = null
    var country: String? = null
    var currency: String? = null
    var isStreamable = false
}
