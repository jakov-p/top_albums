package com.music.topalbums.data.albums

import android.os.Parcelable
import com.music.topalbums.clientapi.albums.Entry
import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.model.ArtistAlbum
import com.music.topalbums.clientapi.model.ArtistAlbumsCollection
import kotlinx.android.parcel.Parcelize

/*
class Album
{
    var artistType: String? = null
    var artistId = 0
    var amgArtistId = 0
    var artistName: String? = null
    var artistLinkUrl: String? = null
    var artistViewUrl: String? = null
    var artworkUrl100: String? = null
    var country: String? = null

    var primaryGenreName: String? = null
    var primaryGenreId = 0

    var collectionId = 0
    var collectionName: String? = null
    var collectionCensoredName: String? = null
    var collectionViewUrl: String? = null
    var collectionPrice = 0.0
    var currency: String? = null

    var trackCount = 0
    var collectionExplicitness: String? = null
    var releaseDate: String? = null  //Date
    var contentAdvisoryRating: String? = null
}


{"wrapperType":"collection", "collectionType":"Album", "artistId":909253, "collectionId":906900960, "amgArtistId":468749, "artistName":"Jack Johnson", "collectionName":"Brushfire Fairytales (Remastered) [Bonus Version]", "collectionCensoredName":"Brushfire Fairytales (Remastered) [Bonus Version]",
"artistViewUrl":"https://music.apple.com/us/artist/jack-johnson/909253?uo=4",
"collectionViewUrl":"https://music.apple.com/us/album/brushfire-fairytales-remastered-bonus-version/906900960?uo=4",
"artworkUrl60":"https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/6c/3a/c5/6c3ac504-80fb-a60f-0f9a-09da88b6b3fc/181229100723.jpg/60x60bb.jpg",
"artworkUrl100":"https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/6c/3a/c5/6c3ac504-80fb-a60f-0f9a-09da88b6b3fc/181229100723.jpg/100x100bb.jpg",
"collectionPrice":9.99, "collectionExplicitness":"notExplicit", "trackCount":15, "copyright":"℗ 2011 Everloving Records", "country":"USA", "currency":"USD", "releaseDate":"2011-04-12T07:00:00Z", "primaryGenreName":"Rock"},
 */

@Parcelize
data class Album(
    val originalPos: Int?,
    val artistName: String?,
    val artistViewUrl: String?,
    val collectionImageUrl: String? = null,
    val collectionViewUrl: String?,
    val primaryGenreName: String? = null,
    val primaryGenreId:Int? = null,
    val collectionId:Int? = null,
    val collectionName: String? = null,
    val collectionPrice: Float? = null,
    val currency: String? = null,

    val trackCount:Int? = null,
    //Date
    val releaseDate: String? = null)  : Parcelable
{
    constructor(originalPos:Int?, artistAlbum: ArtistAlbum) : this(

        originalPos = originalPos,
        artistName = artistAlbum.artistName,
        artistViewUrl = artistAlbum.artistLinkUrl,

        collectionImageUrl = artistAlbum.artworkUrl100,
        collectionViewUrl = artistAlbum.collectionViewUrl,

        primaryGenreName = artistAlbum.primaryGenreName,
        primaryGenreId = artistAlbum.primaryGenreId,

        collectionId = artistAlbum.collectionId,
        collectionName = artistAlbum.collectionName,
        collectionPrice = artistAlbum.collectionPrice,
        currency = artistAlbum.currency,

        trackCount = artistAlbum.trackCount,
        releaseDate = artistAlbum.releaseDate  //Date
    )

    constructor(originalPos:Int?, entry: Entry) : this(
        originalPos = originalPos,
        artistName = entry.im_artist?.label,
        artistViewUrl = entry.im_artist?.attributes?.href,
        collectionImageUrl = entry.im_image?.get(2)?.label,
        collectionViewUrl = entry.id?.label,

        primaryGenreName = entry.category?.attributes?.term,
        primaryGenreId = entry.category?.attributes?.im_id?.toInt(),

        collectionId = entry.id?.attributes?.im_id?.toInt(),
        collectionName = entry.title?.label,
        collectionPrice = entry.im_price?.attributes?.amount?.toFloat(),
        currency = entry.im_price?.attributes?.currency,

        trackCount = entry.im_itemCount?.label?.toInt(),
        releaseDate = entry.im_releaseDate?.label,  //Date
    )

    override fun toString(): String
    {
        val releaseDateShortened = releaseDate?.split("T")?.get(0)
        return "'[$originalPos] $collectionName($primaryGenreName)' by '$artistName', $releaseDateShortened ( $collectionPrice $currency )"
    }

}


class AlbumCollection  (val list: List<Album>)
{
    constructor(topAlbumsCollection: TopAlbumsCollection): this(mutableListOf<Album>())
    {
       topAlbumsCollection.feed?.entry?.forEachIndexed() { i: Int, entry: Entry ->
           (list as MutableList).add(Album(i, entry))
        }
    }


    constructor(artistAlbumsCollection: ArtistAlbumsCollection): this(mutableListOf<Album>())
    {
        artistAlbumsCollection.results?.forEachIndexed { i: Int, artistAlbum: ArtistAlbum ->
            (list as MutableList).add(Album(i, artistAlbum))
        }
    }

}

