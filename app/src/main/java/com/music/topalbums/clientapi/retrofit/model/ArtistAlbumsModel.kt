package com.music.topalbums.clientapi.retrofit.model


class ArtistAlbumsCollection
{
    var resultCount = 0
    var results: ArrayList<ArtistAlbum>? = null
}

class ArtistAlbum
{
    var wrapperType: String? = null
    var artistType: String? = null
    var artistName: String? = null
    var artistLinkUrl: String? = null
    var artistId = 0
    var amgArtistId = 0
    var primaryGenreName: String? = null
    var primaryGenreId = 0
    var collectionType: String? = null
    var collectionId = 0
    var collectionName: String? = null
    var collectionCensoredName: String? = null
    var artistViewUrl: String? = null
    var collectionViewUrl: String? = null
    var artworkUrl60: String? = null
    var artworkUrl100: String? = null
    var collectionPrice: Float = 0.0f
    var collectionExplicitness: String? = null
    var trackCount = 0
    var copyright: String? = null
    var country: String? = null
    var currency: String? = null
    var releaseDate: String? = null  //Date
    var contentAdvisoryRating: String? = null
}

