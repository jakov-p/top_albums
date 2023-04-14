package com.music.topalbums.clientapi.model

/*
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

   {"wrapperType":"track", "kind":"song", "artistId":28673423, "collectionId":1665320666, "trackId":1665320673,
   "artistName":"Fall Out Boy", "collectionName":"So Much (For) Stardust", "trackName":"Love From The Other Side",
   "collectionCensoredName":"So Much (For) Stardust", "trackCensoredName":"Love From The Other Side",
   "artistViewUrl":"https://music.apple.com/us/artist/fall-out-boy/28673423?uo=4",
   "collectionViewUrl":"https://music.apple.com/us/album/love-from-the-other-side/1665320666?i=1665320673&uo=4",
   "trackViewUrl":"https://music.apple.com/us/album/love-from-the-other-side/1665320666?i=1665320673&uo=4",
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/4d/ee/d5/4deed534-da83-6445-23fd-878208dcfb5b/mzaf_10141892996633648524.plus.aac.p.m4a", "artworkUrl30":"https://is5-ssl.mzstatic.com/image/thumb/Music113/v4/20/06/c2/2006c2e0-e05c-bc11-e64c-5fa1677a417d/075679702043.jpg/30x30bb.jpg", "artworkUrl60":"https://is5-ssl.mzstatic.com/image/thumb/Music113/v4/20/06/c2/2006c2e0-e05c-bc11-e64c-5fa1677a417d/075679702043.jpg/60x60bb.jpg", "artworkUrl100":"https://is5-ssl.mzstatic.com/image/thumb/Music113/v4/20/06/c2/2006c2e0-e05c-bc11-e64c-5fa1677a417d/075679702043.jpg/100x100bb.jpg", "collectionPrice":9.99, "trackPrice":1.29, "releaseDate":"2023-02-17T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":1, "trackTimeMillis":279737, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true},
}
*/

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