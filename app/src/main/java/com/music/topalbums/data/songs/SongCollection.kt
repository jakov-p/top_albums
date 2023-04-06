package com.music.topalbums.data.songs

import com.music.topalbums.clientapi.model.AlbumSong
import com.music.topalbums.clientapi.model.AlbumSongsCollection
import com.music.topalbums.clientapi.model.ArtistSong
import com.music.topalbums.clientapi.model.ArtistSongsCollection

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

data class Song(

    val artistId: Int?,
    val collectionId: Int?,
    val amgArtistId: Int?,
    val artistName: String?,

    val country: String?,

    val collectionName: String?,
    val artistViewUrl: String?,

    val collectionPrice: Float?,
    val currency: String?,
    val collectionExplicitness: String?,

    val releaseDate: String?,  //Date
    val primaryGenreName: String?,

    val trackCount: Int?,

    val trackId: Int?,
    val trackName: String?,
    val trackCensoredName: String?,
    val trackViewUrl: String?,
    val previewUrl: String?,
    val trackPrice: Float?,
    val trackExplicitness: String?,
    val trackNumber: Int?,
    val trackTimeMillis: Int?,
    val isStreamable: Boolean)
{
    constructor(albumSong: AlbumSong) : this(
        artistId = albumSong.artistId,
        collectionId = albumSong.collectionId,
        amgArtistId = albumSong.amgArtistId,
        artistName = albumSong.artistName,

        country = albumSong.country,

        collectionName = albumSong.collectionName,
        artistViewUrl = albumSong.artistViewUrl,

        collectionPrice= albumSong.collectionPrice,
        currency = albumSong.currency,
        collectionExplicitness = albumSong.collectionExplicitness,

        releaseDate = albumSong.releaseDate,  //Date
        primaryGenreName = albumSong.primaryGenreName,

        trackCount = albumSong.trackCount,

        trackId = albumSong.trackId,
        trackName= albumSong.trackName,
        trackCensoredName= albumSong.trackCensoredName,
        trackViewUrl= albumSong.trackViewUrl,
        previewUrl= albumSong.previewUrl,
        trackPrice= albumSong.trackPrice,
        trackExplicitness = albumSong.trackExplicitness,
        trackNumber = albumSong.trackNumber,
        trackTimeMillis = albumSong.trackTimeMillis,
        isStreamable= albumSong.isStreamable
    )

    constructor(artistSong: ArtistSong) : this(
        artistId = artistSong.artistId,
        collectionId = artistSong.collectionId,
        amgArtistId = artistSong.amgArtistId,
        artistName = artistSong.artistName,

        country = artistSong.country,

        collectionName = artistSong.collectionName,
        artistViewUrl = artistSong.artistViewUrl,

        collectionPrice= artistSong.collectionPrice,
        currency = artistSong.currency,
        collectionExplicitness = artistSong.collectionExplicitness,

        releaseDate = artistSong.releaseDate,  //Date
        primaryGenreName = artistSong.primaryGenreName,

        trackCount = artistSong.trackCount,

        trackId = artistSong.trackId,
        trackName= artistSong.trackName,
        trackCensoredName= artistSong.trackCensoredName,
        trackViewUrl= artistSong.trackViewUrl,
        previewUrl= artistSong.previewUrl,
        trackPrice= artistSong.trackPrice,
        trackExplicitness = artistSong.trackExplicitness,
        trackNumber = artistSong.trackNumber,
        trackTimeMillis = artistSong.trackTimeMillis,
        isStreamable= artistSong.isStreamable
    )

    override fun toString(): String
    {
        return "($trackNumber)'$trackName' by '$artistName', $releaseDate ( $trackPrice $currency )"
    }
}



class SongCollection private constructor (val list: List<Song>)
{
    constructor(artistSongsCollection: ArtistSongsCollection): this(mutableListOf<Song>())
    {
        artistSongsCollection.results?.forEach {
            (list as MutableList).add(Song(it))
        }
    }

    constructor(albumSongsCollection: AlbumSongsCollection): this(mutableListOf<Song>())
    {
        albumSongsCollection.results?.
        filter { it.wrapperType == "track" }?.
        forEach {
                (list as MutableList).add(Song(it))
            }
    }

}